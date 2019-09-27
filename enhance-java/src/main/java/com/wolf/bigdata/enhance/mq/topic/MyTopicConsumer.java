package com.wolf.bigdata.enhance.mq.topic;

public class MyTopicConsumer implements Runnable {
    private static Thread t1 = null;
    private static Thread t2 = null;

    public static void main(String[] args) {
        t1 = new Thread(new MyTopicConsumer());
        t1.setDaemon(false);
        t1.start();

        t2 = new Thread(new MyTopicConsumer());
        t2.setDaemon(false);
        t2.start();
    }

    public void run() {
        try {
            TopicConsumerTool consumer = new TopicConsumerTool();
            consumer.consumerMessage();
            while (TopicConsumerTool.isConnected) {

            }
        } catch (Exception e) {

        }
    }
}
