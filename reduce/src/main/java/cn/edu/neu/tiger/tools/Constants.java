package cn.edu.neu.tiger.tools;

public class Constants {

	//kafka
	public final static String KAFKA_SOURCE_TOPIC = "metrics";
	public final static String KAFKA_BOOTSTRAP = "bootstrap.servers";
	public final static String KAFKA_BOOTSTRAP_VALUE = " 10.133.24.55:9092";
	//	public final static String KAFKA_BOOTSTRAP_VALUE = "11.227.70.150:9092,11.251.155.243:9092,11.251.155.142:9092";
	public final static String KAFKA_BATCH_SIZE = "batch.size";
	public final static String KAFKA_BUFFER_MEMORY = "buffer.memory";
	public final static String KAFKA_KEY_SERIALIZER = "key.serializer";
	public final static String KAFKA_VALUE_SERIALIZER = "value.serializer";
	public final static String KAFKA_RETRIES = "retries";
	public final static String KAFKA_LINGER_MS = "linger.ms";
	public final static String KAFKA_ACKS = "acks";

}
