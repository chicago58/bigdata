package com.wolf.sortedset;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.Set;

/**
 * @Description 查看英雄出场率排行榜
 * @Author wangqikang
 * @Date 2019-08-28 8:29
 */
public class LolBoxViewer {

    public static void main(String[] args) throws InterruptedException {
        Jedis jedis = new Jedis("centos", 6379);

        int i = 1;
        while (true) {
            // 每隔3秒查看一次榜单
            Thread.sleep(3000);
            System.out.println("第" + i + "次查看榜单：");

            // 从redis中查询榜单的前N名
            Set<Tuple> topHeros = jedis.zrevrangeWithScores("hero", 0, 4);
            for (Tuple tuple : topHeros) {
                System.out.println(tuple.getElement() + " " + tuple.getScore());
            }

            i++;
        }
    }
}
