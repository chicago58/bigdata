package com.wolf.flowsum.partitioner;

import com.wolf.flowsum.bo.FlowBean;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

import java.util.HashMap;

public class ProvincePartitioner extends Partitioner<Text, FlowBean> {

    // 保存查询字典到内存中
    private static HashMap<String, Integer> provinceMap = new HashMap<String, Integer>();

    static {
        provinceMap.put("138", 0);
        provinceMap.put("139", 1);
        provinceMap.put("136", 2);
        provinceMap.put("137", 3);
        provinceMap.put("135", 4);
    }

    public int getPartition(Text text, FlowBean flowBean, int numPartitions) {
        Integer code = provinceMap.get(text.toString().substring(0, 3));
        if (code != null) {
            return code;
        }
        return 5;
    }
}
