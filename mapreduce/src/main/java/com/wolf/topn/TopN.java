package com.wolf.topn;

import com.wolf.topn.bean.OrderBean;
import com.wolf.topn.bo.ItemIdGroupingComparator;
import com.wolf.topn.bo.ItemIdPartitioner;
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

public class TopN {
    public static class TopNMapper extends Mapper<LongWritable, Text, OrderBean, OrderBean> {
        OrderBean bean = new OrderBean();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = StringUtils.split(line, ',');

            bean.set(new Text(fields[0]), new DoubleWritable(Double.parseDouble(fields[2])));
            context.write(bean, bean);
        }
    }

    public static class TopNReducer extends Reducer<OrderBean, OrderBean, NullWritable, OrderBean> {
        int top = 1;
        int count = 0;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            Configuration conf = context.getConfiguration();
            top = Integer.parseInt(conf.get("top"));
        }

        @Override
        protected void reduce(OrderBean key, Iterable<OrderBean> values, Context context) throws IOException, InterruptedException {
            for (OrderBean value : values) {
                if ((count++) == top) {
                    count = 0;
                    return;
                }
                context.write(NullWritable.get(), value);
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        conf.set("top", "1");
        Job job = Job.getInstance(conf);

        job.setJarByClass(TopN.class);
        job.setMapperClass(TopNMapper.class);
        job.setReducerClass(TopNReducer.class);

        job.setMapOutputKeyClass(OrderBean.class);
        job.setMapOutputValueClass(OrderBean.class);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(OrderBean.class);

        job.setPartitionerClass(ItemIdPartitioner.class);
        job.setNumReduceTasks(1);

        job.setGroupingComparatorClass(ItemIdGroupingComparator.class);

        FileInputFormat.setInputPaths(job, new Path("../bigdata/mapreduce/src/main/resources/topn/"));
        FileOutputFormat.setOutputPath(job, new Path("../bigdata/mapreduce/src/main/resources/top-n"));

        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }
}
