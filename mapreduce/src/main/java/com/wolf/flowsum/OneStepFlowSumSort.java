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
import org.apache.hadoop.util.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class OneStepFlowSumSort {

    public static class OneStepFlowSumSortMapper extends Mapper<LongWritable, Text, Text, FlowBean> {
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

    /**
     * 在reduce端实现一次性汇总和排序操作
     */
    public static class OneStepFlowSumSortReducer extends Reducer<Text, FlowBean, Text, FlowBean> {
        // 所有的统计汇总数据保存到内存
        TreeMap<FlowBean, Text> treeMap = new TreeMap<FlowBean, Text>();

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

            Text phoneObj = new Text(key);
            treeMap.put(flowBean, phoneObj);
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            Set<Map.Entry<FlowBean, Text>> entries = treeMap.entrySet();
            for (Map.Entry<FlowBean, Text> entry : entries) {
                context.write(entry.getValue(), entry.getKey());
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJarByClass(OneStepFlowSumSort.class);

        job.setMapperClass(OneStepFlowSumSortMapper.class);
        job.setReducerClass(OneStepFlowSumSortReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        FileInputFormat.setInputPaths(job, new Path("../bigdata/mapreduce/src/main/resources/flowsum/"));
        FileOutputFormat.setOutputPath(job, new Path("../bigdata/mapreduce/src/main/resources/flowsum/output"));

        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }
}
