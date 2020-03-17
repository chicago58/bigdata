package com.wolf;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;

/**
 * @Description HDFS Java API
 * @Author wangqikang
 * @CreatedAt 2020-03-06 21:42
 **/
public class HdfsDemo {
    private static FileSystem fs = null;

    public static void main(String[] args) throws Exception {
        init();

        //上传文件到HDFS
//        fs.copyFromLocalFile(new Path("F:\\1.txt"), new Path("/"));

        //从HDFS下载文件
        //写入文件时需要操作本地文件系统的权限
//        fs.copyToLocalFile(new Path("/1.txt"), new Path("F:\\2.txt"));
        //使用原生的Java操作本地文件系统
//        fs.copyToLocalFile(false, new Path("/1.txt"), new Path("F:\\2.txt"), true);

        //创建目录
//        fs.mkdirs(new Path("/aa/bb/cc"));

        //重命名
//        fs.rename(new Path("/aa/bb/cc"), new Path("/aa/bb/cc1"));

        //删除目录，如果目录非空，则第二个参数必须为true
//        fs.delete(new Path("/aa"), true);

        //查看目录信息，只显示文件
//        listFiles();

        //查看文件及目录信息
        listAll();

        fs.close();
    }

    /**
     * 查看目录和文件信息
     *
     * @throws Exception
     */
    private static void listAll() throws Exception {
        FileStatus[] allNodes = fs.listStatus(new Path("/"));
        String flag = "";
        for (FileStatus temp : allNodes) {
            if (temp.isFile()) {
                flag = "f--";
            } else {
                flag = "d--";
            }
            System.out.println(flag + temp.getPath().getName());
        }
    }

    /**
     * 显示文件信息
     *
     * @throws Exception
     */
    private static void listFiles() throws Exception {
        RemoteIterator<LocatedFileStatus> iterator = fs.listFiles(new Path("/"), true);
        while (iterator.hasNext()) {
            LocatedFileStatus file = iterator.next();
            System.out.println(file.getPath().getName());
            System.out.println(file.getBlockSize());
            System.out.println(file.getPermission());
            System.out.println(file.getLen());
            BlockLocation[] blockLocations = file.getBlockLocations();
            for (BlockLocation location : blockLocations) {
                System.out.println("block-length: " + location.getLength() + " -- " + "block-offset: " + location.getOffset());
                String[] hosts = location.getHosts();
                for (String host : hosts) {
                    System.out.println(host);
                }
            }
            System.out.println("============分割线============");
        }
    }

    /**
     * 获取文件系统客户端对象
     *
     * @throws Exception
     */
    private static void init() throws Exception {
        Configuration conf = new Configuration();

        //获取HDFS客户端对象时需要指定文件系统类型为HDFS，并指定客户端用户身份（仅Windows用户）
        //设置文件系统的类型
        conf.set("fs.defaultFS", "hdfs://mini1:9000");
        //修改JVM参数，设置用户身份
        System.setProperty("HADOOP_USER_NAME", "root");

        //获取HDFS客户端对象
        fs = FileSystem.get(conf);
        //同时设置文件系统类型和用户身份
//        fs = FileSystem.get(new URI("hdfs://mini1:9000"), conf, "root");

        // 获取文件系统相关信息
        DatanodeInfo[] dataNodeStats = ((DistributedFileSystem) fs).getDataNodeStats();
        for (DatanodeInfo info : dataNodeStats) {
            System.out.println(info.getHostName());
        }
    }
}
