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
import java.util.Arrays;

public class CommonFriendStepTwo {
    public static class CommonFriendStepTwoMapper extends Mapper<LongWritable, Text, Text, Text> {
        Text personObj = new Text();
        Text friendObj = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] tokens = line.split("\t");

            String friend = tokens[0];
            friendObj.set(friend);
            String[] persons = tokens[1].split("-");

            Arrays.sort(persons);

            for (int i = 0; i < persons.length; i++) {
                for (int j = i + 1; j < persons.length; j++) {
                    personObj.set(persons[i] + "-" + persons[j]);
                    context.write(personObj, friendObj);
                }
            }
        }
    }

    public static class CommonFriendStepTwoReducer extends Reducer<Text, Text, Text, Text> {
        Text friendObj = new Text();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            StringBuilder stringBuffer = new StringBuilder();

            for (Text friend : values) {
                stringBuffer.append(friend).append(" ");
            }

            friendObj.set(stringBuffer.toString());
            context.write(key, friendObj);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(CommonFriendStepTwo.class);
        job.setMapperClass(CommonFriendStepTwoMapper.class);
        job.setReducerClass(CommonFriendStepTwoReducer.class);
        job.setCombinerClass(CommonFriendStepTwoReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job, new Path("../bigdata/mapreduce/src/main/resources/commonfriend-1/"));
        FileOutputFormat.setOutputPath(job, new Path("../bigdata/mapreduce/src/main/resources/commonfriend-2"));

        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }
}
