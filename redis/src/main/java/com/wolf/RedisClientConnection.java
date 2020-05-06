package com.wolf;

import redis.clients.jedis.Jedis;

/**
 * @Description 测试 Redis服务器与客户端的连通性
 * @Author wangqikang
 * @Date 2019-08-27 07:53
 */
public class RedisClientConnection {

    public static void main(String[] args) {
        // 构造 Redis客户端对象
        Jedis jedis = new Jedis("localhost", 6379);
        // 测试是否连接到服务器
        String ping = jedis.ping();
        System.out.println(ping);
    }
}
