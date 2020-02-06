package com.wolf.topone.bean;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class OrderBean implements WritableComparable<OrderBean> {
    private Text itemId;
    private DoubleWritable amount;

    public Text getItemId() {
        return itemId;
    }

    public void setItemId(Text itemId) {
        this.itemId = itemId;
    }

    public DoubleWritable getAmount() {
        return amount;
    }

    public void setAmount(DoubleWritable amount) {
        this.amount = amount;
    }

    public OrderBean() {
    }

    public OrderBean(Text itemId, DoubleWritable amount) {
        set(itemId, amount);
    }

    public void set(Text itemId, DoubleWritable amount) {
        this.itemId = itemId;
        this.amount = amount;
    }

    public int compareTo(OrderBean o) {
        int cmp = this.itemId.compareTo(o.getItemId());
        if (cmp == 0) {
            cmp = -this.amount.compareTo(o.getAmount());
        }
        return cmp;
    }

    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(itemId.toString());
        dataOutput.writeDouble(amount.get());
    }

    public void readFields(DataInput dataInput) throws IOException {
        String readUTF = dataInput.readUTF();
        double readDouble = dataInput.readDouble();

        this.itemId = new Text(readUTF);
        this.amount = new DoubleWritable(readDouble);
    }

    @Override
    public String toString() {
        return "itemId=" + itemId +
                ", amount=" + amount;
    }
}
