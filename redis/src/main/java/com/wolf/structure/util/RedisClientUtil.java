package com.wolf.structure.util;

import redis.clients.jedis.Jedis;

public class RedisClientUtil {

    /**
     * 获取 Redis客户端连接对象
     *
     * @param host
     * @param port
     * @return
     */
    public static Jedis getRedisConnection(String host, int port) {
        return new Jedis(host, port);
    }
}
