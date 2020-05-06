package com.wolf.bigdata.enhance.blockingqueue;

import java.util.concurrent.BlockingQueue;

/**
 * @Description 模拟消费者消费消息
 * @Author wangqikang
 * @Date 2019-09-04 08:37
 */
public class Consumer implements Runnable {
    BlockingQueue<String> queue;

    public Consumer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            String consumer = Thread.currentThread().getName();
            System.out.println(consumer);
            // 如果队列为空，则阻塞当前线程
            String message = queue.take();
            System.out.println(consumer + "消费者 get a product: " + message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
