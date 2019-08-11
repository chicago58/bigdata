package com.wolf.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
 * KEYIN 是指框架读取数据的key的类型
 * 默认InputFormat下，key是一行文本的起始偏移量，所以key的类型是Long.
 * <p>
 * VALUEIN 是指框架读取数据的value的类型
 * 默认InputFormat下，value是一行文本的内容，所以value的类型是String.
 * <p>
 * KEYOUT 是指用户自定义逻辑方法返回数据的key的类型（由用户业务逻辑决定）
 * wordcount程序中，输出的key是单词，所以key的类型是String.
 * <p>
 * VALUEOUT 是指用户自定义逻辑方法返回数据的value的类型（由用户业务逻辑决定）
 * wordcount程序中，输出的value是单词的数量，所以value的类型是Integer.
 * <p>
 * 但是 String/Long 等 JDK 自带的数据类型在序列化时效率较低
 * hadoop为了提高序列化效率，自定义了一套序列化框架
 * 所以在hadoop程序中，如果数据需要序列化（写磁盘、网络传输），就一定要用实现了hadoop序列化框架的数据类型
 * Long --> LongWritable
 * String --> Text
 * Integer --> IntWritable
 * Null --> NullWritable
 *
 * @Description
 * @Author wangqikang
 * @Date 2019-08-09 22:51
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    /**
     * map() 是mapreduce框架主体运行程序MapTask所要调用的用户业务逻辑方法
     * MapTask 驱动 InputFormat 读取数据到（KEYIN, VALUEIN），每读到一个 KV 对，就调用一次 map()
     * <p>
     * 默认InputFormat实现中，key为一行的起始偏移量，value为一行的内容
     *
     * @param key
     * @param value
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] words = line.split(" ");
        for (String word : words) {
            context.write(new Text(word), new IntWritable(1));
        }
    }
}
