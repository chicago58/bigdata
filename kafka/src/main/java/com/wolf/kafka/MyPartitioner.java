package com.wolf.kafka;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

/**
 * @Description 自定义partition
 * @Author wangqikang
 * @Date 2019-09-11 07:16
 */
public class MyPartitioner implements Partitioner {
    public MyPartitioner(VerifiableProperties properties) {
    }

    public int partition(Object key, int numPartitions) {
        return 3;
    }
}
/**
 * 自定义partition的基本步骤：
 * 1.实现Partitioner类
 * 2.添加自定义Partitioner构造器
 * 3.将自定义Partitioner添加到properties中
 * 4.发送消息时指定partitionKey
 */
