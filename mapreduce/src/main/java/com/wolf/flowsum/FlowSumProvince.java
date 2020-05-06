package com.wolf.flowsum;

import com.wolf.flowsum.partitioner.ProvincePartitioner;
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
import org.apache.hadoop.util.StringUtils;

import java.io.IOException;

public class FlowSumProvince {

    public static class FlowSumProvinceMapper extends Mapper<LongWritable, Text, Text, FlowBean> {
        Text phoneObj = new Text();
        FlowBean flowBean = new FlowBean();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = StringUtils.split(line, '\t');
            String phoneStr = fields[1];
            long upFlow = Long.parseLong(fields[fields.length - 3]);
            long downFlow = Long.parseLong(fields[fields.length - 2]);

            phoneObj.set(phoneStr);
            flowBean.setMutiFields(upFlow, downFlow);
            context.write(phoneObj, flowBean);
        }
    }

    public static class FlowSumProvinceReducer extends Reducer<Text, FlowBean, Text, FlowBean> {
        @Override
        protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
            int upCount = 0;
            int downCount = 0;
            for (FlowBean bean : values) {
                upCount += bean.getUpFlow();
                downCount += bean.getDownFlow();
            }

            FlowBean flowBean = new FlowBean();
            flowBean.setMutiFields(upCount, downCount);
            context.write(key, flowBean);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJarByClass(FlowSumProvince.class);

        job.setMapperClass(FlowSumProvinceMapper.class);
        job.setReducerClass(FlowSumProvinceReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        // 设置Shuffle 分区组件为自定义分区组件
        job.setPartitionerClass(ProvincePartitioner.class);
        // 设置ReduceTask数量（与分区数相同）
        job.setNumReduceTasks(6);

        FileInputFormat.setInputPaths(job, new Path("../bigdata/mapreduce/src/main/resources/flowsum/"));
        FileOutputFormat.setOutputPath(job, new Path("../bigdata/mapreduce/src/main/resources/flowsum/province"));

        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }
}
