package com.wolf.ack;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Map;

/**
 * @Description TODO
 * @Author wangqikang
 * @CreatedAt 2020-03-21 17:24
 **/
public class Bolt4 extends BaseRichBolt {
    private OutputCollector collector;

    /**
     * 初始化方法，只调用一次
     *
     * @param map
     * @param topologyContext
     * @param outputCollector
     */
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
    }

    /**
     * 被循环调用
     *
     * @param tuple
     */
    public void execute(Tuple tuple) {
        collector.emit(tuple, new Values(tuple.getString(0)));
        System.out.println("Bolt4的execute()方法被调用一次" + tuple.getString(0));
        //告诉Storm框架Bolt4的处理状态
        collector.ack(tuple);
    }

    /**
     * 定义输出字段
     *
     * @param outputFieldsDeclarer
     */
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("uuid"));
    }
}
