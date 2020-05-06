package com.wolf.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description 创建可缓存的线程池，可重用老的可用线程，不用每次新建线程。
 * @Author wangqikang
 * @CreatedAt 2020-03-05 18:26
 **/
public class CachedThreadPoolDemo {

    private static ExecutorService executor = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        for (int i = 0; i < 7; i++) {
            final int index = i;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("第" + index + "个线程" + Thread.currentThread().getName());
                }
            });

        }
    }
}
