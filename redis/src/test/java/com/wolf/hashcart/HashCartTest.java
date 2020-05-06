package com.wolf.hashcart;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.Set;

/**
 * @Description 使用redis的hash结构模拟购物车功能
 * @Author wangqikang
 * @Date 2019-08-28 7:46
 */
public class HashCartTest {

    private Jedis jedis = null;
    private static final String CART_PRIFIX = "cart:";

    @Before
    public void init() {
        jedis = new Jedis("centos", 6379);
    }

    /**
     * 添加商品到购物车
     */
    @Test
    public void testAddItemToCart() {
        jedis.hset(CART_PRIFIX + "user02", "肥皂", "2");
        jedis.hset(CART_PRIFIX + "user02", "手铐", "1");
        jedis.hset(CART_PRIFIX + "user02", "皮鞭", "3");
        jedis.hset(CART_PRIFIX + "user02", "蜡烛", "4");
        jedis.close();
    }

    /**
     * 查询购物车信息
     */
    @Test
    public void testGetCartInfo() {
        Map<String, String> cart = jedis.hgetAll(CART_PRIFIX + "user02");
        Set<Map.Entry<String, String>> entries = cart.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        jedis.close();
    }

    /**
     * 更改购物车
     */
    @Test
    public void editCart() {
        jedis.hincrBy(CART_PRIFIX + "user02", "蜡烛", 1);
        jedis.close();
    }

    /**
     * 从购物车中删除商品项
     */
    @Test
    public void delItemFromCart() {
        jedis.hdel(CART_PRIFIX + "user02", "肥皂");
        jedis.close();
    }
}
