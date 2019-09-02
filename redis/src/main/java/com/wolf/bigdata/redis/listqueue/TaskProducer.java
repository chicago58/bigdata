package com.wolf.bigdata.redis.listqueue;

import redis.clients.jedis.Jedis;

import java.util.Random;
import java.util.UUID;

/**
 * @Description 模拟生产者不断向队列中产生任务
 * @Author wangqikang
 * @Date 2019-08-27 22:22
 */
public class TaskProducer {

    /**
     * 获取redis客户端连接对象
     *
     * @param host
     * @param port
     * @return
     */
    private static Jedis getRedisConnection(String host, int port) {
        return new Jedis(host, port);
    }

    public static void main(String[] args) {
        Jedis jedis = getRedisConnection("centos", 6379);

        Random random = new Random();
        // 生产任务
        while (true) {
            try {
                // 生产任务的速度有一定的随机性，在1-2秒之间
                Thread.sleep(random.nextInt(1000) + 1000);
                // 生产一个任务
                String taskId = UUID.randomUUID().toString();
                // 向list中插入任务，第一次插入时list不存在，但是lpush()方法会在redis中创建一条新的list数据
                jedis.lpush("task-queue", taskId);
                System.out.println("向任务队列中插入一条新的任务：" + taskId);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
