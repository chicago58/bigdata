package com.wolf.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/**
 * 本类是客户端用来指定wordcount job程序运行时所需的参数：
 * 指定哪个组件作为数据读取器、数据结果输出器
 * 指定哪个类作为map阶段的业务逻辑类、哪个类作为reduce阶段的业务逻辑类
 * 指定wordcount job程序的jar包所在路径
 * ...
 * 以及其他各种参数
 *
 * @Description
 * @Author wangqikang
 * @Date 2019-08-09 07:46
 */
public class WordCountDriver {

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration(); // 加载hadoop配置参数
        Job job = Job.getInstance(conf);

        // 程序jar包所在路径
        job.setJar("../bigdata/mapreduce/target/mapreduce-1.0-SNAPSHOT.jar");

        // 程序所用Mapper和Reducer类
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        // Mapper和Reducer输出数据类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 数据读取和结果输出所用组件
        job.setInputFormatClass(TextInputFormat.class); // TextInputFormat 默认读取文本文件，偏移量作为key，内容作为value
        job.setOutputFormatClass(TextOutputFormat.class); // TextOutputFormat 默认写到文本文件中

        // 要处理文件的路径
        FileInputFormat.setInputPaths(job, new Path("../bigdata/mapreduce/src/main/resources/wordcount/"));

        // 处理结果的输出路径
        FileOutputFormat.setOutputPath(job, new Path("../bigdata/mapreduce/src/main/resources/wordcount/output"));

        /**
         * waitForCompletion() 底层调用submit()方法
         * submit()调用之前，Driver先与集群建立通道，接收任务执行进度
         */
        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }
}
