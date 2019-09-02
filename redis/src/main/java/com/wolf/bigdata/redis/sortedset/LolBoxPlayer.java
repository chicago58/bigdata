package com.wolf.bigdata.redis.sortedset;

import redis.clients.jedis.Jedis;

import java.util.Random;

/**
 * @Description 使用redis的sortedset模拟LOL英雄出场率榜单
 * @Author wangqikang
 * @Date 2019-08-28 8:22
 */
public class LolBoxPlayer {

    public static void main(String[] args) throws InterruptedException {
        Jedis jedis = new Jedis("centos", 6379);

        Random random = new Random();
        String[] heros = {"易大师", "德邦", "剑姬", "盖伦", "阿卡丽", "金克斯", "提莫", "猴子", "亚索"};
        while (true) {
            // 随机选择英雄
            int index = random.nextInt(heros.length);
            String hero = heros[index];

            // 模拟玩游戏
            Thread.sleep(1000);

            // 集合中英雄的出场次数加1
            // 第一次添加时集合不存在，zincrby()创建
            jedis.zincrby("hero", 1, hero);

            System.out.println(hero + " 出场了 ... ");
        }
    }
}
