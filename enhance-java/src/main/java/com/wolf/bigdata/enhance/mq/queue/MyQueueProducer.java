package com.wolf.bigdata.enhance.mq.queue;

import javax.jms.JMSException;

public class MyQueueProducer {
    public static void main(String[] args) throws JMSException {
        QueueProducerTool producer = new QueueProducerTool();

        for (int i = 100; i < 120; i++) {
            producer.produceMessage("hello world!" + i);
        }
        // 生产者生产消息时是多线程的方式，多线程没有结束，所以主线程无法关闭
        producer.close();
    }
}
