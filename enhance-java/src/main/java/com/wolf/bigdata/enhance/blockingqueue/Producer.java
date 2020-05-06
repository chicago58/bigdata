package com.wolf.bigdata.enhance.blockingqueue;

import java.util.concurrent.BlockingQueue;

/**
 * @Description 模拟生产者创建消息
 * @Author wangqikang
 * @Date 2019-09-04 08:34
 */
public class Producer implements Runnable {
    BlockingQueue<String> queue;

    public Producer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            String message = "A product, 生产线程： " + Thread.currentThread().getName();
            // 如果队列已满，则阻塞当前线程
            queue.put(message);
            System.out.println("生产者 I have made a product: " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
