package com.wolf.inputformat;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;

/**
 * @Description TODO
 * @Author wangqikang
 * @CreatedAt 2020-03-29 19:04
 **/
public class BiDelimiterInputFormat extends TextInputFormat {

    @Override
    public RecordReader<LongWritable, Text> getRecordReader(InputSplit genericSplit, JobConf job, Reporter reporter) throws IOException {
        reporter.setStatus(genericSplit.toString());

//        BiRecordReader reader = new BiRecordReader(job, (FileSplit) genericSplit);

        MyRecordReader reader = new MyRecordReader(new LineRecordReader(job, (FileSplit) genericSplit));
        return reader;
    }

    public static class MyRecordReader implements RecordReader<LongWritable, Text> {
        LineRecordReader reader;
        Text text;

        public MyRecordReader(LineRecordReader reader) {
            this.reader = reader;
            this.text = reader.createValue();
        }

        public boolean next(LongWritable longWritable, Text text) throws IOException {
            while (reader.next(longWritable, text)) {
                String strReplace = text.toString().toLowerCase().replaceAll("\\|\\|", "\\|");
                Text txtReplace = new Text();
                txtReplace.set(strReplace);
                text.set(txtReplace.getBytes(), 0, txtReplace.getLength());
                return true;
            }
            return false;
        }

        public LongWritable createKey() {
            return reader.createKey();
        }

        public Text createValue() {
            return new Text();
        }

        public long getPos() throws IOException {
            return reader.getPos();
        }

        public void close() throws IOException {
            reader.close();
        }

        public float getProgress() throws IOException {
            return reader.getProgress();
        }
    }
}


