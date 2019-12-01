package com.sschat.content.consumer;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class Consumer {

    static List<String> users = new ArrayList<>();

    public static void init() {
        users = new ArrayList<>();
    //    users.add("Bevis");
    //    users.add("bill");
    //    users.add("bishop");

        users.add("");
        users.add(null);
    }

    public static void consume() throws InterruptedException {
        init();
        while (true) {
            Thread.sleep(30);
            int index = RandomUtil.randomInt(users.size());
            String user = users.get(index);
            sendRequest(user);
        }
    }

    static long succCount = 0;
    static long errCount = 0;

    public static void sendRequest(String user) {
        HttpRequest request;
        if (user != null) {
            request = HttpUtil.createGet("http://node3/index" + "?user=" + user);
        } else {
            request = HttpUtil.createGet("http://node3/index");
        }
        try {
            TimeInterval timer = new TimeInterval(true);
            log.info("request|{}", request.getUrl());
            HttpResponse response = request.execute();
            log.info("response|{}", response.body());
            long diff = timer.interval();
            if (response.isOk()) {
                log.info("metrics|{},{},{},{},{},{}", EnvProperties.JOB_NAME, EnvProperties.PodName, "latency", System.currentTimeMillis()/1000, diff, "value");
                log.info("metrics|{},{},{},{},{},{}", EnvProperties.JOB_NAME, EnvProperties.PodName, "successQPS", System.currentTimeMillis()/1000, ++succCount, "counter");
            } else {
                log.info("metrics|{},{},{},{},{},{}", EnvProperties.JOB_NAME, EnvProperties.PodName, "failQPS", System.currentTimeMillis()/1000, ++errCount, "counter");
            }
        } catch (Exception e) {
            log.info("metrics|{},{},{},{},{},{}", EnvProperties.JOB_NAME, EnvProperties.PodName, "errorQPS", System.currentTimeMillis()/1000, ++errCount, "counter");
            log.error("error|{}", e.getMessage(), e);
        }


    }
}
