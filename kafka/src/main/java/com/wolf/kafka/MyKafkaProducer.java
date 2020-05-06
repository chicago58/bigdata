package com.wolf.kafka;


import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

/**
 * @Description 模拟生产者生产消息
 * @Author wangqikang
 * @Date 2019-08-11 22:01
 */
public class MyKafkaProducer {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("metadata.broker.list", "localhost:9092");
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        properties.put("partitioner.class", "com.wolf.kafka.MyPartitioner");

        Producer producer = new Producer(new ProducerConfig(properties));
        while (true) {
            // 生产数据
            producer.send(new KeyedMessage("order3","test", "我爱我的祖国!"));
        }
    }
}
