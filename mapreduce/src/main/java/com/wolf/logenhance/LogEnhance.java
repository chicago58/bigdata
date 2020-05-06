package com.wolf.logenhance;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;

public class LogEnhance {
    static class LogEnhanceMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
        HashMap<String, String> knowledgeMap = new HashMap<String, String>();

        Text text = new Text();

        /**
         * MapTask在初始化时先调用一次setup()
         * 在setup()方法中，将外部资源加载到内存
         *
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            DBLoader.loadDB(knowledgeMap);
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = StringUtils.split("\t");

            try {
                String url = fields[26];

                // url匹配知识库
                String content = knowledgeMap.get(url);

                // 构造输出结果
                String result = "";
                if (content == null) {
                    // 写入带爬列表
                    result = url + "\t" + "tocrawl\n";
                } else {
                    // 写入增强日志
                    result = line + "\t" + content + "\t";
                }

                text.set(result);
                context.write(text, NullWritable.get());
            } catch (Exception e) {

            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(LogEnhance.class);
        job.setMapperClass(LogEnhanceMapper.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        // 设置自定义输出组件
        job.setOutputFormatClass(LogEnhanceOutputFormat.class);

        FileInputFormat.setInputPaths(job, new Path(""));
        // OutputFormat继承自FileOutputFormat，FileOutputFormat输出_SUCCESS文件，所以需要指定输出目录
        FileOutputFormat.setOutputPath(job, new Path(""));

        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }
}
