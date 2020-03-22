package com.wolf.wordcount;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * @Description TODO
 * @Author wangqikang
 * @CreatedAt 2020-03-07 21:33
 **/
public class LineSplitBolt extends BaseBasicBolt {
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        //获取数据
        String line = (String) tuple.getValueByField("line");

        //数据切割
        String[] fields = line.split(" ");

        //发送数据
        for (String word : fields) {
            //Values对象会生成一个List
            basicOutputCollector.emit(new Values(word, 1));
        }
    }

    /**
     * 定义输出
     *
     * @param outputFieldsDeclarer
     */
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("word", "num"));
    }
}
