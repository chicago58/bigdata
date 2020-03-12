package com.wolf;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author wangqikang
 * @CreatedAt 2020-03-07 21:32
 **/
public class LocalFileSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;

    private BufferedReader reader;

    /**
     * 初始化
     *
     * @param map
     * @param topologyContext
     * @param spoutOutputCollector
     */
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.collector = spoutOutputCollector;
        try {
            this.reader = new BufferedReader(new FileReader(new File("/1.log")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 循环处理数据
     */
    public void nextTuple() {

        try {
            String line = reader.readLine();

            if (StringUtils.isNotBlank(line)) {
                List<Object> arrayList = new ArrayList<Object>();
                arrayList.add(line);

                //nextTuple()方法每调用一次就会发送一条数据
                collector.emit(arrayList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 定义输出
     *
     * @param outputFieldsDeclarer
     */
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("line"));
    }
}
