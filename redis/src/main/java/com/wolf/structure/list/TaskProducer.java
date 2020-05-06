package com.wolf.structure.list;

import com.wolf.structure.util.RedisClientUtil;
import redis.clients.jedis.Jedis;

import java.util.Random;
import java.util.UUID;

/**
 * @Description 模拟生产者不断向队列生产任务
 * @Author wangqikang
 * @Date 2019-08-27 22:22
 */
public class TaskProducer {

    public static void main(String[] args) {
        Jedis jedis = RedisClientUtil.getRedisConnection("localhost", 6379);

        Random random = new Random();
        // 生产任务
        while (true) {
            try {
                // 生产任务的速度有一定的随机性，在1-2秒之间
                Thread.sleep(random.nextInt(1000) + 1000);
                // 生产一个任务
                String taskId = UUID.randomUUID().toString();
                // 向队列中插入任务
                jedis.lpush("task-queue", taskId);
                System.out.println("向任务队列中插入一条新的任务：" + taskId);
                System.out.println("任务队列：" + jedis.lrange("task-queue", 0, -1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
