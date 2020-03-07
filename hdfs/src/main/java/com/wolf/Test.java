package com.wolf;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * @Description TODO
 * @Author wangqikang
 * @CreatedAt 2020-03-06 21:42
 **/
public class Test {

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        //获取HDFS客户端对象时需要指定文件系统类型为HDFS，并指定客户端用户身份（仅Windows用户）
        //设置文件系统的类型
        conf.set("fs.defaultFS", "hdfs://mini1:9000");
        //修改JVM参数，设置用户身份
        System.setProperty("HADOOP_USER_NAME", "root");

        //获取HDFS客户端对象
        FileSystem fs = FileSystem.get(conf);
        //同时设置文件系统类型和用户身份
//        FileSystem fs = FileSystem.get(new URI("hdfs://mini1:9000"), conf, "root");

        //上传文件到HDFS
        fs.copyFromLocalFile(new Path(""), new Path(""));

        //从HDFS下载文件
        //写入文件时需要操作本地文件系统的权限
//        fs.copyToLocalFile(new Path(""), new Path(""));
        //使用原始Java方式操作本地文件系统
        fs.copyToLocalFile(false, new Path(""), new Path(""), true);


        fs.close();
    }
}
