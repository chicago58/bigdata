package com.wolf.ack.basebasicbolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.FailedException;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

/**
 * @Description TODO
 * @Author wangqikang
 * @CreatedAt 2020-03-21 17:24
 **/
public class Bolt3 extends BaseBasicBolt {

    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
//        basicOutputCollector.emit(new Values(tuple.getString(0)));

        //抛出异常
        throw new FailedException("异常");
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
