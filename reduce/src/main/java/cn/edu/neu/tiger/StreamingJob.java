/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.edu.neu.tiger;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple6;
import org.apache.flink.api.java.tuple.Tuple9;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

public class StreamingJob {

    static Logger log = LoggerFactory.getLogger(StreamingJob.class);

    public static void main(String[] args) throws Exception {
        // set up the streaming execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties properties = new Properties();
        //这里是由一个kafka
        properties.setProperty("bootstrap.servers", "server.hhchat.cn:9092");
//        properties.setProperty("bootstrap.servers", "10.133.24.55:9092");
        //properties.setProperty("group.id", "flink_consumer");
        //第一个参数是topic的名称
        DataStream<String> source = env.addSource(new FlinkKafkaConsumer010("metrics", new SimpleStringSchema(), properties));
        System.out.println("--------------------------------------------");
        DataStream<Tuple6<String, String, String, Long, Double, String>> formatSource = source.flatMap(new FlatMapFunction<String, Tuple6<String, String, String, Long, Double, String>>() {
            @Override
            public void flatMap(String source, Collector<Tuple6<String, String, String, Long, Double, String>> collector) throws Exception {
                if (!source.contains("metrics|")) {
                    return;
                }
                String info = source.substring(source.indexOf("metrics|")).trim();
                info = info.substring(8);
                String[] values = info.split(",");
                String jobName = values[0];
                String nodeName = values[1];
                String counterName = values[2];
                Long timestamp = -1L;
                Double counterValue = -1.0;
                try {
                    String timeInStr = values[3];
                    if (timeInStr.contains(".")) {
                        timeInStr = timeInStr.substring(0, timeInStr.indexOf('.'));
                    }
                    timestamp = Long.parseLong(timeInStr);
                    counterValue = Double.valueOf(values[4].trim());
                } catch (Exception e) {
                    log.error("error log: {}", source);
                    return;
                }
                String counterType = values[5];
                collector.collect(Tuple6.of(jobName, nodeName, counterName, timestamp, counterValue, counterType));
            }
        }).assignTimestampsAndWatermarks(new AscendingTimestampExtractor<Tuple6<String, String, String, Long, Double, String>>() {
            @Override
            public long extractAscendingTimestamp(Tuple6<String, String, String, Long, Double, String> tuple6) {
                return tuple6.f3;
            }
        });

        DataStream<Tuple6<String, String, String, Long, Double, String>> valueStream = formatSource.filter((FilterFunction<Tuple6<String, String, String, Long, Double, String>>) tuple6 -> tuple6.f5.equals("value"));

        DataStream<Tuple9<Long, Double, String, String, String, Long, Long, Long, String>> valueResult = valueStream
                .keyBy(0, 1, 2)
                .timeWindow(Time.seconds(30))
                .apply(new WindowFunction<Tuple6<String, String, String, Long, Double, String>, Tuple9<Long, Double, String, String, String, Long, Long, Long, String>, Tuple, TimeWindow>() {
                    @Override
                    public void apply(Tuple tuple, TimeWindow timeWindow, Iterable<Tuple6<String, String, String, Long, Double, String>> iterable, Collector<Tuple9<Long, Double, String, String, String, Long, Long, Long, String>> collector) throws Exception {
                        Long startTimestamp = Long.MAX_VALUE;
                        Long endTimestamp = Long.MIN_VALUE;//f0 时间戳
                        Double value = 0.0;//f1 值
                        String countType = "value";//f2 计数类型
                        String taskName = tuple.getFieldNotNull(0);//f3 任务名称
                        String nodeName = tuple.getFieldNotNull(1);//f4 节点名称
                        Long count = 0L;//f5 时间段内点的个数
                        Long startTime = timeWindow.getStart();//f6 查询时间段起始时间
                        Long endTime = timeWindow.getEnd();//f7 查询时间段结束时间
                        String counterName = tuple.getFieldNotNull(2);// f8 counter名称

                        for (Tuple6<String, String, String, Long, Double, String> tuple6 : iterable) {
                            startTimestamp = Math.min(startTimestamp, tuple6.f3);
                            endTimestamp = Math.max(endTimestamp, tuple6.f3);
                            value += tuple6.f4;
                            count++;
                        }

                        if (count == 0) {
                            return;
                        }

                        value = value / count;

                        collector.collect(Tuple9.of(endTimestamp, value, countType, taskName, nodeName, count, startTime, endTime, counterName));
                    }
                });

        DataStream<Tuple6<String, String, String, Long, Double, String>> counterStream = formatSource.filter((FilterFunction<Tuple6<String, String, String, Long, Double, String>>) tuple6 -> tuple6.f5.equals("counter"));

        DataStream<Tuple9<Long, Double, String, String, String, Long, Long, Long, String>> counterResult = counterStream
                .keyBy(0, 1, 2)
                .timeWindow(Time.seconds(30))
                .apply(new WindowFunction<Tuple6<String, String, String, Long, Double, String>, Tuple9<Long, Double, String, String, String, Long, Long, Long, String>, Tuple, TimeWindow>() {
                    @Override
                    public void apply(Tuple tuple, TimeWindow timeWindow, Iterable<Tuple6<String, String, String, Long, Double, String>> iterable, Collector<Tuple9<Long, Double, String, String, String, Long, Long, Long, String>> collector) throws Exception {
                        Long startTimestamp = Long.MAX_VALUE;
                        Long endTimestamp = Long.MIN_VALUE;//f0 时间戳

                        Double startValue = Double.MAX_VALUE;
                        Double endValue = Double.MIN_VALUE;

                        Double value = 0.0;//f1 值
                        String countType = "counter";//f2 计数类型
                        String taskName = tuple.getFieldNotNull(0);//f3 任务名称
                        String nodeName = tuple.getFieldNotNull(1);//f4 节点名称
                        Long count = 0L;//f5 时间段内点的个数
                        Long startTime = timeWindow.getStart();//f6 查询时间段起始时间
                        Long endTime = timeWindow.getEnd();//f7 查询时间段结束时间
                        String counterName = tuple.getFieldNotNull(2);// f8 counter名称

                        for (Tuple6<String, String, String, Long, Double, String> tuple6 : iterable) {
                            if (tuple6.f3 < startTimestamp) {
                                startTimestamp = tuple6.f3;
                                startValue = tuple6.f4;
                            }
                            if (tuple6.f3 > endTimestamp) {
                                endTimestamp = tuple6.f3;
                                endValue = tuple6.f4;
                            }
                            count++;
                        }
                        if (count == 0) {
                            return;
                        }
                        long range = (endTimestamp - startTimestamp);
                        if (range <= 0) {
                            range = 30L;
                        }

                        value = (endValue - startValue) * 1.0 / range;

//                        value = (endValue - startValue) / (endTime - startTime);

                        collector.collect(Tuple9.of(endTimestamp, value, countType, taskName, nodeName, count, startTime, endTime, counterName));
                    }
                });

        valueResult.print();
        counterResult.print();
        valueResult.addSink(new MysqlSink());
        counterResult.addSink(new MysqlSink());
//         execute program
        env.execute("metrics");
    }


    public static class MysqlSink extends
            RichSinkFunction<Tuple9<Long, Double, String, String, String, Long, Long, Long, String>> {
        private Connection connection;
        private PreparedStatement preparedStatement;
        String username = "cdb_outerroot";
        String password = "1wechatPassword!";
        String drivername = "com.mysql.jdbc.Driver";            //配置改成自己的配置
        String dburl = "jdbc:mysql://58c4e0ef73145.bj.cdb.myqcloud.com:12248/wsep_data?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false";

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            Class.forName(drivername);
            connection = DriverManager.getConnection(dburl, username, password);
            String sql = "insert into wsep_data(last_point_time,counter_value,counter_type,job_name,pod_name,point_count, time_range_start, time_range_end,counter_name) values(?,?,?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
        }

        @Override
        public void invoke(Tuple9<Long, Double, String, String, String, Long, Long, Long, String> value) throws Exception {
            try {
                preparedStatement.setLong(1, value.f0);
                preparedStatement.setDouble(2, value.f1);
                preparedStatement.setString(3, value.f2);
                preparedStatement.setString(4, value.f3);
                preparedStatement.setString(5, value.f4);
                preparedStatement.setLong(6, value.f5);
                preparedStatement.setLong(7, value.f6);
                preparedStatement.setLong(8, value.f7);
                preparedStatement.setString(9, value.f8);
                preparedStatement.execute();
            } catch (Exception e) {
                log.error("error");
            }

        }

        @Override
        public void close() throws Exception {
            super.close();
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static class MyFilterFunction implements FilterFunction<String> {
        @Override
        public boolean filter(String s) throws Exception {
            if (s.startsWith("metrics")) {
                return true;
            }
            return false;
        }
    }

//    public static class TimestampExtractor implements AssignerWithPeriodicWatermarks<Tuple6<String, String, String, Long, Double, String>> {
//
//        @Nullable
//        @Override
//        public Watermark getCurrentWatermark() {
//            return new Watermark(System.currentTimeMillis());
//        }
//
//        @Override
//        public long extractTimestamp(Tuple6<String, String, String, Long, Double, String> element, long previousElementTimestamp) {
//            return element.f3;
//        }
//    }


}
