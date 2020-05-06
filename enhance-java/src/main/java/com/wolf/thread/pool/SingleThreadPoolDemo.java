package com.wolf.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description 创建单线程线程池
 * @Author wangqikang
 * @CreatedAt 2020-03-05 18:26
 **/
public class SingleThreadPoolDemo {

    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        for (int i = 0; i < 7; i++) {
            final int number = i;

            executor.execute(new Runnable() {

                @Override
                public void run() {
                    System.out.println("现在的时间:" + System.currentTimeMillis() + "第" + number + "个线程");

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }
}
