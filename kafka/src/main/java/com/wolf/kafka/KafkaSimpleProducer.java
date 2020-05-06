package com.wolf.kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

/**
 * @Description
 * @Author wangqikang
 * @Date 2019-09-09 07:49
 */
public class KafkaSimpleProducer {

    public static void main(String[] args) {
        /**
         * 1.指定当前kafka producer生产数据的目的地
         *
         * 在kafka集群的任一节点输入命令创建topic：
         * bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test
         */
        String TOPIC = "order2";

        /**
         * 2.读取配置文件
         */
        Properties props = new Properties();

        /**
         * key.serializer.class默认为serializer.class
         */
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        /**
         * kafka的broker主机，格式为host1:port1,host2:port2
         */
        props.put("metadata.broker.list", "localhost:9092");

        /**
         * 3.通过配置文件创建生产者
         */
        Producer producer = new Producer<String, String>(new ProducerConfig(props));

        /**
         * 4.for循环生产数据
         */
        while (true) {
            String messageStr = "aaa";

            /**
             * 5.调用producer的send()方法发送数据
             * 若自定义partition，需要指定partitionKey
             */
            producer.send(new KeyedMessage<String, String>(TOPIC, messageStr));
        }


    }
}
