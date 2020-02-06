package com.wolf.index;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class IndexCreateStepTwo {
    public static class IndexCreateStepTwoMapper extends Mapper<LongWritable, Text, Text, Text> {
        Text wordObj = new Text();
        Text index = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split("\t");
            String wordAndFile = fields[0];
            String count = fields[1];

            String[] words = wordAndFile.split("->");
            String word = words[0];
            String fileName = words[1];

            wordObj.set(word);
            index.set(fileName + "->" + count);
            context.write(wordObj, index);
        }
    }

    public static class IndexCreateStepTwoReducer extends Reducer<Text, Text, Text, Text> {
        Text value = new Text();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            StringBuilder stringBuilder = new StringBuilder();
            for (Text value : values) {
                stringBuilder.append(value).append(" ");
            }
            value.set(stringBuilder.toString());
            context.write(key, value);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(IndexCreateStepTwo.class);
        job.setMapperClass(IndexCreateStepTwoMapper.class);
        job.setReducerClass(IndexCreateStepTwoReducer.class);
        job.setCombinerClass(IndexCreateStepTwoReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job, new Path("../bigdata/mapreduce/src/main/resources/index-1/"));
        FileOutputFormat.setOutputPath(job, new Path("../bigdata/mapreduce/src/main/resources/index-2"));

        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }
}
