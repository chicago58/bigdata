package com.wolf.bigdata.enhance.mq.topic;

import javax.jms.JMSException;
import java.util.Random;

public class MyTopicProducer {
    public static void main(String[] args) throws JMSException, InterruptedException {
        TopicProducerTool producer = new TopicProducerTool();
        Random random = new Random();
        for (int i = 30; i < 50; i++) {
            Thread.sleep(random.nextInt(10) * 1000);

            producer.produceMessage("hello world " + i);
            producer.close();
        }
    }
}
