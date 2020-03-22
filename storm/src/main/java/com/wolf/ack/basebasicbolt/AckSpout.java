package com.wolf.ack.basebasicbolt;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.Map;
import java.util.UUID;

/**
 * @Description TODO
 * @Author wangqikang
 * @CreatedAt 2020-03-21 17:09
 **/
public class AckSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;

    /**
     * 初始化方法
     *
     * @param map
     * @param topologyContext
     * @param spoutOutputCollector
     */
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.collector = spoutOutputCollector;
    }

    /**
     * 循环调用，每调用一次就发送一条消息
     */
    public void nextTuple() {
        //生成一条数据
        String uuid = UUID.randomUUID().toString().replace("_", "");
        //发送消息并指定msgId
        collector.emit(new Values(uuid), uuid);

        try {
            Thread.sleep(10 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 定义发送的字段
     *
     * @param outputFieldsDeclarer
     */
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("uuid"));
    }

    /**
     * 消息处理成功的回调
     *
     * @param msgId
     */
    @Override
    public void ack(Object msgId) {
        System.out.println("消息处理成功" + msgId);
    }

    /**
     * 消息处理失败的回调
     *
     * @param msgId
     */
    @Override
    public void fail(Object msgId) {
        System.out.println("消息处理失败：重发" + msgId);
        //消息处理失败后重发
        collector.emit(new Values(msgId), msgId);
    }
}
