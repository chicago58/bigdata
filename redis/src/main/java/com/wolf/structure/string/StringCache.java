package com.wolf.structure.string;

import com.google.gson.Gson;
import com.wolf.bo.ProductInfo;
import com.wolf.structure.util.RedisClientUtil;
import redis.clients.jedis.Jedis;

import java.io.*;

/**
 * Redis中 String数据结构最适合做缓存
 */
public class StringCache {

    private static Jedis jedis = RedisClientUtil.getRedisConnection("localhost", 6379);

    public static void stringCache() {
        jedis.set("user02:name", "ruhua");
        jedis.set("user03:name", "siyu");

        String u02 = jedis.get("user02:name");
        String u03 = jedis.get("user03:name");

        System.out.println(u02);
        System.out.println(u03);
    }

    /**
     * 1.将自定义对象序列化为字节数组，并缓存到Redis的String结构中
     * 2.从Redis的String结构中获取字节数组，并反序列化为自定义对象
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void convertObjectToByteArrayCache() throws IOException, ClassNotFoundException {
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

        // 将对象序列化后的byte数组存储到Redis的String结构中
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
     * 1.将自定义对象转换成json字符串，并缓存到Redis的String结构中
     * 2.从Redis的String结构中获取json字符串，并转换成对象
     */
    public static void convertObjectToJsonCache() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("mac book");
        productInfo.setDescription("mac book computer");
        productInfo.setCatelog("unknown");
        productInfo.setPrice(11000);

        // 利用Gson将对象转换成json串
        Gson gson = new Gson();
        String productJson = gson.toJson(productInfo);

        // 将json串存储到Redis中
        jedis.set("product:02", productJson);

        // 从Redis中取出对象的json串
        String jsonResp = jedis.get("product:02");

        // 从json串解析出对象
        ProductInfo productResp = gson.fromJson(jsonResp, ProductInfo.class);
        System.out.println(productResp);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // String数据结构常用来做缓存
        stringCache();

        // 自定义对象序列化为二进制数组后存储到Redis中、从Redis中读取二进制数组反序列化为自定义对象
        convertObjectToByteArrayCache();

        // 自定义对象通过Gson解析成json字符串存储到Redis中、从Redis中读取json字符串转换成自定义对象
        convertObjectToJsonCache();
    }
}
