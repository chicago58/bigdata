package com.wolf.kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description
 * @Author wangqikang
 * @Date 2019-09-09 08:05
 */
public class KafkaSimpleConsumer implements Runnable {
    private String title;
    private KafkaStream<byte[], byte[]> stream;

    public KafkaSimpleConsumer(String title, KafkaStream<byte[], byte[]> stream) {
        // 获取消费编号和kafkaStream
        this.title = title;
        this.stream = stream;
    }

    public void run() {
        System.out.println("开始运行：" + title);

        /**
         * 6.从kafkaStream中获取迭代器
         */
        ConsumerIterator<byte[], byte[]> iterator = stream.iterator();

        /**
         * 7.从stream读取消息，等待新消息时hasNext()会阻塞
         */
        while (iterator.hasNext()) {
            MessageAndMetadata<byte[], byte[]> data = iterator.next();
            String topic = (String) data.topic();
            int partition = data.partition();
            long offset = data.offset();
            String msg = new String(data.message());
            System.out.println(String.format("Consumer: [%s], Topic: [%s], PartitionId: [%d], Offset: [%d], msg: [%s]", title, topic, partition, offset, msg));
        }
    }

    public static void main(String[] args) {
        /**
         * 1.准备配置参数
         */
        Properties props = new Properties();
        props.put("group.id", "testGroup");
        props.put("zookeeper.connect", "localhost:2181");
        props.put("auto.offset.reset", "largest");
        props.put("auto.commit.interval.ms", "1000");
        props.put("partition.assignment.strategy", "roundrobin");
        ConsumerConfig config = new ConsumerConfig(props);

        /**
         * 2.消费的topic
         */
        String topic = "order3";

        /**
         * 3.创建consumer连接器
         * 只要ConsumerConnector存在，消费者一直等待消息，不会退出
         */
        ConsumerConnector consumerConn = Consumer.createJavaConsumerConnector(config);

        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, 1);


        /**
         * 4.获取每个topic对应的kafkaStream
         */
        Map<String, List<KafkaStream<byte[], byte[]>>> topicStreamMap = consumerConn.createMessageStreams(topicCountMap);

        /**
         * 5.消费kafkaStream中的数据
         */
        List<KafkaStream<byte[], byte[]>> streams = topicStreamMap.get(topic);

        ExecutorService executor = Executors.newFixedThreadPool(4);
        for (int i = 0; i < streams.size(); i++) {
            executor.execute(new KafkaSimpleConsumer("消费者" + (i + 1), streams.get(i)));
        }
    }
}
