package com.wolf.bigdata.redis.listqueue;

import redis.clients.jedis.Jedis;

import java.util.Random;

/**
 * @Description 模拟消费者处理任务
 * @Author wangqikang
 * @Date 2019-08-27 22:22
 */
public class TaskConsumer {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("centos", 6379);
        Random random = new Random();
        while (true) {
            try {
                // 从task-queue中获取任务，同时放入temp-queue中
                String taskId = jedis.rpoplpush("task-queue", "temp-queue");

                // 模拟任务处理
                Thread.sleep(1000);

                // 模拟任务处理成功和失败的情况
                int nextInt = random.nextInt(13);
                if (nextInt % 7 == 0) {
                    // 失败时需要将任务从temp-queue弹回至task-queue中
                    jedis.rpoplpush("temp-queue", "task-queue");
                    System.out.println("任务处理失败：" + taskId);
                } else {
                    // 成功时将任务从temp-queue中清除
                    jedis.rpop("temp-queue");
                    System.out.println("任务处理成功：" + taskId);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
