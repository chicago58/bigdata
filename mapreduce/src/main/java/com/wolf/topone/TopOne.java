package com.wolf.topone;

import com.wolf.topone.bean.OrderBean;
import com.wolf.topone.bo.ItemIdGroupingComparator;
import com.wolf.topone.bo.ItemIdPartitioner;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.StringUtils;

import java.io.IOException;

/**
 * 通过SecondarySort机制实现订单金额最大的记录
 */
public class TopOne {
    public static class TopOneMapper extends Mapper<LongWritable, Text, OrderBean, NullWritable> {
        OrderBean bean = new OrderBean();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = StringUtils.split(line, ',');

            bean.set(new Text(fields[0]), new DoubleWritable(Double.parseDouble(fields[2])));
            context.write(bean, NullWritable.get());
        }
    }

    public static class TopOneReducer extends Reducer<OrderBean, NullWritable, OrderBean, NullWritable> {
        @Override
        protected void reduce(OrderBean key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            context.write(key, NullWritable.get());
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(TopOne.class);
        job.setMapperClass(TopOneMapper.class);
        job.setReducerClass(TopOneReducer.class);

        job.setOutputKeyClass(OrderBean.class);
        job.setOutputValueClass(NullWritable.class);

        // 指定Shuffle使用的Partitioner
        job.setPartitionerClass(ItemIdPartitioner.class);
        job.setNumReduceTasks(1);

        // 指定Shuffle使用的GroupComparator
        job.setGroupingComparatorClass(ItemIdGroupingComparator.class);

        FileInputFormat.setInputPaths(job, new Path("../bigdata/mapreduce/src/main/resources/topn/"));
        FileOutputFormat.setOutputPath(job, new Path("../bigdata/mapreduce/src/main/resources/top-1"));

        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }
}
