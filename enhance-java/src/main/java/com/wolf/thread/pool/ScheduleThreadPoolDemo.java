package com.wolf.thread.pool;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description 创建可以调度的线程池，指定任务定时执行
 * @Author wangqikang
 * @CreatedAt 2020-03-05 18:26
 **/
public class ScheduleThreadPoolDemo {

    private static ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(2);

    public static void main(String[] args) {

        //执行定时任务
        System.out.println("现在的时间:" + System.currentTimeMillis());
        executor.schedule(new Runnable() {

            @Override
            public void run() {
                System.out.println("现在的时间:" + System.currentTimeMillis());

            }
        }, 4, TimeUnit.SECONDS);//设置延迟4秒执行

        //执行固定周期的重复任务
        System.out.println("现在的时间:" + System.currentTimeMillis());
        executor.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                System.out.println("现在的时间:" + System.currentTimeMillis());
            }
        }, 2, 3, TimeUnit.SECONDS);//设置延迟2秒后每3秒执行一次
    }

}
