package com.wolf.bigdata.redis.stringcache;

import com.google.gson.Gson;
import com.wolf.bigdata.redis.bo.ProductInfo;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.*;

/**
 * @Description redis中String数据结构最适合做缓存
 * @Author wangqikang
 * @Date 2019-08-27 20:14
 */
public class StringStructureDataTest {

    private Jedis jedis = null;

    @Before
    public void init() {
        jedis = new Jedis("centos", 6379);
    }

    @Test
    public void testString() {
        jedis.set("user02:name", "ruhua");
        jedis.set("user03:name", "siyu");

        String u02 = jedis.get("user02:name");
        String u03 = jedis.get("user03:name");

        System.out.println(u02);
        System.out.println(u03);
    }

    /**
     * 1.将自定义对象序列化为字节数组，并缓存到redis的String结构中
     * 2.从redis的String结构中获取字节数组，并反序列化为对象
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Test
    public void testConvertObjectToByteArrayCache() throws IOException, ClassNotFoundException {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("mac book");
        productInfo.setDescription("mac book computer");
        productInfo.setCatelog("unknown");
        productInfo.setPrice(10000);

        // 将对象序列化为字节数组
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(ba);

        // 使用对象输出流序列化自定义对象，并把序列化后的二进制数据写入ba流
        oos.writeObject(productInfo);

        // 将ba流转换成byte数组
        byte[] bytes = ba.toByteArray();

        // 将对象序列化后的byte数组存储到redis的String结构中
        jedis.set("product:01".getBytes(), bytes);

        byte[] bytesResp = jedis.get("product:01".getBytes());

        // 将byte数组反序列化为对象
        ByteArrayInputStream bi = new ByteArrayInputStream(bytesResp);
        ObjectInputStream oi = new ObjectInputStream(bi);

        // 从对象输入流中读取自定义对象
        ProductInfo productResp = (ProductInfo) oi.readObject();
        System.out.println(productResp);
    }

    /**
     * 1.将自定义对象转换成json字符串，并缓存到redis的String结构中
     * 2.从redis的String结构中获取json字符串，并转换成对象
     */
    @Test
    public void testConvertObjectToJsonCache() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("mac book");
        productInfo.setDescription("mac book computer");
        productInfo.setCatelog("unknown");
        productInfo.setPrice(11000);

        // 利用gson将对象转换成json串
        Gson gson = new Gson();
        String productJson = gson.toJson(productInfo);

        // 将json串存储到redis中
        jedis.set("product:02", productJson);

        // 从redis中取出对象的json串
        String jsonResp = jedis.get("product:02");

        // 从json串中解析出对象
        ProductInfo productResp = gson.fromJson(jsonResp, ProductInfo.class);
        System.out.println(productResp);
    }
}
