package com.wolf.bigdata.redis.bo;

import java.io.Serializable;

/**
 * @Description
 * @Author wangqikang
 * @Date 2019-08-27 8:32
 */
public class ProductInfo implements Serializable {
    private String name;
    private String description;
    private String catelog;
    private double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCatelog() {
        return catelog;
    }

    public void setCatelog(String catelog) {
        this.catelog = catelog;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ProductInfo{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", catelog='" + catelog + '\'' +
                ", price=" + price +
                '}';
    }
}
