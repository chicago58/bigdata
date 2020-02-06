package com.wolf.commonfriend;

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

public class CommonFriendStepOne {

    public static class CommonFriendStepOneMapper extends Mapper<LongWritable, Text, Text, Text> {
        Text friendObj = new Text();
        Text personObj = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] tokens = line.split(":");

            String person = tokens[0];
            personObj.set(person);
            String[] friends = tokens[1].split(",");

            for (String friend : friends) {
                friendObj.set(friend);
                context.write(friendObj, personObj);
            }
        }
    }

    public static class CommonFriendStepOneReducer extends Reducer<Text, Text, Text, Text> {
        Text personObj = new Text();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            StringBuilder stringBuffer = new StringBuilder();
            for (Text person : values) {
                stringBuffer.append(person).append("-");
            }
            personObj.set(stringBuffer.toString());
            context.write(key, personObj);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(CommonFriendStepOne.class);

        job.setMapperClass(CommonFriendStepOneMapper.class);
        job.setReducerClass(CommonFriendStepOneReducer.class);
        job.setCombinerClass(CommonFriendStepOneReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job, new Path("../bigdata/mapreduce/src/main/resources/commonfriend/"));
        FileOutputFormat.setOutputPath(job, new Path("../bigdata/mapreduce/src/main/resources/commonfriend-1"));

        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }
}
