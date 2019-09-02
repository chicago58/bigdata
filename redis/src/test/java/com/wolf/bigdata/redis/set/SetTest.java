package com.wolf.bigdata.redis.set;

import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * @Description redis中set集合的api
 * @Author wangqikang
 * @Date 2019-08-28 7:59
 */
public class SetTest {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("centos", 6379);

        jedis.sadd("girlfriends", "wangyijia", "xuyuting", "fengyao");
        jedis.sadd("boyfriends", "laoda", "laoer", "laosan");

        // 判断一个成员是否属于指定的set集合
        Boolean isOrNot = jedis.sismember("girlfriends", "wangzhijie");
        System.out.println(isOrNot);

        // 求两个集合的差集、并集、交集
        Set<String> sdiff = jedis.sdiff("girlfriends", "boyfriends");
        Set<String> sunion = jedis.sunion("girlfriends", "boyfriends");
        Set<String> sinter = jedis.sinter("girlfriends", "boyfriends");

        for (String ele : sunion) {
            System.out.println(ele);
        }
    }
}
