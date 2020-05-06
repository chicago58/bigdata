package com.wolf.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description 创建固定数量线程池，指定最大线程数
 * @Author wangqikang
 * @CreatedAt 2020-03-05 18:26
 **/
public class FixedThreadPoolDemo {

    private static ExecutorService executor = Executors.newFixedThreadPool(5);//设置最大线程数为5

    public static void main(String[] args) {
        for (int i = 0; i < 7; i++) {
            final int index = i;

            executor.execute(new Runnable() {

                @Override
                public void run() {
                    System.out.println("时间:" + System.currentTimeMillis() + "第" + index + "个线程" + Thread.currentThread().getName());
                    try {
                        Thread.sleep(2000);//由于设置最大线程为5，所以当执行完前5个线程后，需要等待两秒，再执行后两个线程。
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
