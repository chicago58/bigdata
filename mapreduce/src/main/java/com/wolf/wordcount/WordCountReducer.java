package com.wolf.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @Description
 * @Author wangqikang
 * @Date 2019-08-09 07:46
 */
public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    /**
     * mapreduce框架主体程序 ReduceTask 收到map阶段所有MapTask输出数据的一部分
     * MapTask输出数据的key.hashcode % ReduceTask数 == 本 ReduceTask 号
     * <p>
     * ReduceTask 处理 KV 数据时调用 reduce()：
     * （1）先将所有 KV 对按key是否相同分组；
     * （2）将每一组中第一个 KV 对的key传给reduce()的key变量，将这一组 KV 中所有value用迭代器传给reduce()的values变量；
     *
     * @param key
     * @param values
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int count = 0;
        for (IntWritable value : values) {
            count += value.get();
        }
        context.write(key, new IntWritable(count));
    }
}