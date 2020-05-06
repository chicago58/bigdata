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

public class FlowSum {

    /**
     * 在KV中传输自定义对象，必须实现Hadoop序列化机制(implements Writable)
     */
    public static class FlowSumMapper extends Mapper<LongWritable, Text, Text, FlowBean> {
        FlowBean flowBean = new FlowBean();
        Text phoneObj = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            // 字段切分
            String[] fields = StringUtils.split(line, '\t');
            // 字段抽取
            String phoneStr = fields[1];
            long upFlow = Long.parseLong(fields[fields.length - 3]);
            long downFlow = Long.parseLong(fields[fields.length - 2]);
//            FlowBean flowBean = new FlowBean(upFlow, downFlow); // 数据量较大时会创建大量对象
            flowBean.setMutiFields(upFlow, downFlow);

//            context.write(new Text(phoneStr), flowBean); // 数据量较大时会创建大量对象
            phoneObj.set(phoneStr);
            context.write(phoneObj, flowBean);
        }
    }

    public static class FlowSumReducer extends Reducer<Text, FlowBean, Text, FlowBean> {
        FlowBean flowBean = new FlowBean();

        /**
         * key是手机号，values是该组所有Bean的迭代器
         *
         * @param key
         * @param values
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
            long upFlowCount = 0;
            long downFlowCount = 0;
            for (FlowBean bean : values) {
                upFlowCount += bean.getUpFlow();
                downFlowCount += bean.getDownFlow();
            }

            flowBean.setMutiFields(upFlowCount, downFlowCount);
            context.write(key, flowBean);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(FlowSum.class);
        job.setMapperClass(FlowSumMapper.class);
        job.setReducerClass(FlowSumReducer.class);

        // 如果Map输出的KV类型与Reducer一致，只需要写Reduce输出的KV类型
//        job.setMapOutputKeyClass(Text.class);
//        job.setMapOutputValueClass(FlowBean.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        // TextInputFormat、TextOutputFormat是框架默认的输入输出组件，可省略
//        job.setInputFormatClass(TextInputFormat.class);
//        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.setInputPaths(job, new Path("../bigdata/mapreduce/src/main/resources/flowsum/"));
        FileOutputFormat.setOutputPath(job, new Path("../bigdata/mapreduce/src/main/resources/flowsum/sum"));

        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }
}
