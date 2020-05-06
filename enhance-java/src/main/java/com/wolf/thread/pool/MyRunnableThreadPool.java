package com.wolf.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description 使用线程池newCachedThreadPool来执行线程
 * @Author wangqikang
 * @CreatedAt 2020-03-05 18:15
 **/
public class MyRunnableThreadPool {
    public static void main(String[] args) throws InterruptedException {
        // 创建线程池
        ExecutorService executor = Executors.newCachedThreadPool();

        for (int i = 1; i < 50; i++) {
            executor.execute(new Runnable() {
                public void run() {
                    System.out.println("thread name: " + Thread.currentThread().getName());

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        Thread.sleep(10000);

        for (int i = 1; i < 50; i++) {
            executor.execute(new Runnable() {
                public void run() {
                    System.out.println("thread name: " + Thread.currentThread().getName());

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        executor.shutdown();
    }
}
