package com.wolf.topn.bo;

import com.wolf.topone.bean.OrderBean;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;


/**
 * 控制Shuffle过程中ReduceTask对KV对的聚合逻辑
 */
public class ItemIdGroupingComparator extends WritableComparator {

    /**
     * 无参构造
     */
    protected ItemIdGroupingComparator() {
        // 自定义分组的数据类型
        super(OrderBean.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        OrderBean aBean = (OrderBean) a;
        OrderBean bBean = (OrderBean) b;

        // itemId相同的Bean对象聚合为一组
        return aBean.getItemId().compareTo(bBean.getItemId());
    }
}
