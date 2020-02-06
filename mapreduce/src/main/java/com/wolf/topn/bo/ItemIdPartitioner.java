package com.wolf.topn.bo;

import com.wolf.topone.bean.OrderBean;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

public class ItemIdPartitioner extends Partitioner<OrderBean, NullWritable> {
    public int getPartition(OrderBean orderBean, NullWritable nullWritable, int numPartitions) {
        // itemId相同的Bean对象发往相同的ReduceTask
        return (orderBean.getItemId().hashCode() & Integer.MAX_VALUE) % numPartitions;
    }
}
