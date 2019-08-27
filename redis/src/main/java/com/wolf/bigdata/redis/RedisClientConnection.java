package com.wolf.bigdata.redis;

import redis.clients.jedis.Jedis;

/**
 * @Description 测试redis服务器与客户端的连通性
 * @Author wangqikang
 * @Date 2019-08-27 07:53
 */
public class RedisClientConnection {

    public static void main(String[] args) {
        // 构造redis客户端对象
        Jedis jedis = new Jedis("centos", 6379);
        String ping = jedis.ping();
        System.out.println(ping);
    }
}
