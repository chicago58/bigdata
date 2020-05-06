package com.wolf.logenhance;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * <Text, NullWritable> 为最终输出的KV类型
 */
public class LogEnhanceOutputFormat extends FileOutputFormat<Text, NullWritable> {
    /**
     * 构造RecordWriter
     *
     * @param job
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public RecordWriter<Text, NullWritable> getRecordWriter(TaskAttemptContext job) throws IOException, InterruptedException {
        FileSystem fs = FileSystem.get(job.getConfiguration());
        Path enhancePath = new Path("/");
        Path toCrawlPath = new Path("");

        FSDataOutputStream enhanceOut = fs.create(enhancePath);
        FSDataOutputStream toCrawlOut = fs.create(toCrawlPath);

        return new MyRecordWriter(enhanceOut, toCrawlOut);
    }

    static class MyRecordWriter extends RecordWriter<Text, NullWritable> {
        FSDataOutputStream enhanceOut = null;
        FSDataOutputStream toCrawlOut = null;

        public MyRecordWriter(FSDataOutputStream enhanceOut, FSDataOutputStream toCrawlOut) {
            this.enhanceOut = enhanceOut;
            this.toCrawlOut = toCrawlOut;
        }

        /**
         * 数据输出逻辑
         *
         * @param key
         * @param value
         * @throws IOException
         * @throws InterruptedException
         */
        public void write(Text key, NullWritable value) throws IOException, InterruptedException {
            // 判断如果包含tocrawl，则写入待爬列表中
            if (key.toString().contains("tocrawl")) {
                toCrawlOut.write(key.toString().getBytes());
            } else {
                enhanceOut.write(key.toString().getBytes());
            }
        }

        public void close(TaskAttemptContext context) throws IOException, InterruptedException {
            if (toCrawlOut != null) {
                toCrawlOut.close();
            }
            if (enhanceOut != null) {
                enhanceOut.close();
            }
        }
    }
}
