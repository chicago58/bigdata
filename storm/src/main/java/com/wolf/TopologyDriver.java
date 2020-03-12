package com.wolf;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;

/**
 * @Description Storm程序驱动类
 * @Author wangqikang
 * @CreatedAt 2020-03-10 22:44
 **/
public class TopologyDriver {

    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
        //准备任务信息
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("LocalFileSpout", new LocalFileSpout());
        topologyBuilder.setBolt("LineSplitBolt", new LineSplitBolt()).shuffleGrouping("LocalFileSpout");
        topologyBuilder.setBolt("WordCountBolt", new WordCountBolt()).shuffleGrouping("LineSplitBolt");

        //提交任务
        Config config = new Config();
        StormTopology topology = topologyBuilder.createTopology();

        //本地模式
        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("wordcount", config, topology);

        //集群模式
//        StormSubmitter.submitTopology("wordcount", config, topology);
    }
}
