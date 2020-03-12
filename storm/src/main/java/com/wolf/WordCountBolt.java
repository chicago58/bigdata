package com.wolf;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Author wangqikang
 * @CreatedAt 2020-03-07 21:33
 **/
public class WordCountBolt extends BaseBasicBolt {
    private Map<String, Integer> wordCountMap = new HashMap<String, Integer>();

    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        String word = (String) tuple.getValueByField("word");
        Integer num = (Integer) tuple.getValueByField("num");

        Integer integer = wordCountMap.get(word);
        if (integer == null || integer.intValue() == 0) {
            wordCountMap.put(word, num);
        } else {
            wordCountMap.put(word, integer.intValue() + num);
        }

        //打印
        System.out.println(wordCountMap);

    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        //todo 打印出来，所以不需要定义输出的字段
    }
}
