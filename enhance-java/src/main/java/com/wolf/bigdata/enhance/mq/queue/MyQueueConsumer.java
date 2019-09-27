package com.wolf.bigdata.enhance.mq.queue;

public class MyQueueConsumer implements Runnable {
    private static Thread t1 = null;
    private static Thread t2 = null;

    public static void main(String[] args) {
        t1 = new Thread(new MyQueueConsumer());
        t1.start();

        t2 = new Thread(new MyQueueConsumer());
        t2.start();
    }

    public void run() {
        try {
            QueueConsumerTool consumer = new QueueConsumerTool();
            consumer.consumeMessage();
            while (QueueConsumerTool.isConnected) {

            }
        } catch (Exception e) {

        }
    }
}
