package com.wolf.flowsum;

import com.wolf.flowsum.bo.FlowBean;
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

/**
 * 实现流量汇总并按照流量大小倒序排序[前提：处理的是已经汇总过的数据文件]
 */
public class FlowSumSort {
    public static class FlowSumSortMapper extends Mapper<LongWritable, Text, FlowBean, Text> {
        FlowBean flowBean = new FlowBean();
        Text phoneObj = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split("\t");
            String phoneStr = fields[0];
            long upFlowSum = Long.parseLong(fields[1]);
            long downFlowSum = Long.parseLong(fields[2]);
            flowBean.setMutiFields(upFlowSum, downFlowSum);

            phoneObj.set(phoneStr);
            context.write(flowBean, phoneObj);
        }
    }

    public static class FlowSumSortReducer extends Reducer<FlowBean, Text, Text, FlowBean> {
        @Override
        protected void reduce(FlowBean key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            context.write(values.iterator().next(), key);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJarByClass(FlowSumSort.class);

        job.setMapperClass(FlowSumSortMapper.class);
        job.setReducerClass(FlowSumSortReducer.class);

        job.setMapOutputKeyClass(FlowBean.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        FileInputFormat.setInputPaths(job, new Path("../bigdata/mapreduce/src/main/resources/flowsum/sum/"));
        FileOutputFormat.setOutputPath(job, new Path("../bigdata/mapreduce/src/main/resources/flowsum/sort"));

        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }
}
