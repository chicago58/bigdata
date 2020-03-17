package com.wolf.wordindex;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class IndexCreateStepOne {
    public static class IndexCreateStepOneMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        Text text = new Text();
        IntWritable num = new IntWritable(1);

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] words = line.split(" ");

            // 通过上下文context获取正在处理的文件切片信息（context包含了本次处理数据的文件信息）
            FileSplit fileSplit = (FileSplit) context.getInputSplit();
            // 获取文件名
            String fileName = fileSplit.getPath().getName();

            // 输出key：单词->文件名，value：1
            for (String word : words) {
                text.set(word + "->" + fileName);
                context.write(text, num);
            }
        }
    }

    public static class IndexCreateStepOneReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        IntWritable num = new IntWritable();

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int count = 0;
            for (IntWritable value : values) {
                count += value.get();
            }
            num.set(count);
            context.write(key, num);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(IndexCreateStepOne.class);

        job.setMapperClass(IndexCreateStepOneMapper.class);
        job.setReducerClass(IndexCreateStepOneReducer.class);
        job.setCombinerClass(IndexCreateStepOneReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.setInputPaths(job, new Path("../bigdata/mapreduce/src/main/resources/index/"));
        FileOutputFormat.setOutputPath(job, new Path("../bigdata/mapreduce/src/main/resources/index-1"));
        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }
}
