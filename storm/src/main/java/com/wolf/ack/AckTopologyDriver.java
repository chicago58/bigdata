package com.wolf.ack;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;

/**
 * @Description TODO
 * @Author wangqikang
 * @CreatedAt 2020-03-21 17:31
 **/
public class AckTopologyDriver {

    public static void main(String[] args) {
        //准备任务信息
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("AckSpout", new AckSpout(), 1);
        topologyBuilder.setBolt("Bolt1", new Bolt1(), 1).shuffleGrouping("AckSpout");
        topologyBuilder.setBolt("Bolt2", new Bolt2(), 1).shuffleGrouping("Bolt1");
        topologyBuilder.setBolt("Bolt3", new Bolt3(), 1).shuffleGrouping("Bolt2");
        topologyBuilder.setBolt("Bolt4", new Bolt4(), 1).shuffleGrouping("Bolt3");

        Config config = new Config();
        config.setNumWorkers(2);
        StormTopology topology = topologyBuilder.createTopology();

        //提交本地模式
        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("Ack", config, topology);
    }

}
