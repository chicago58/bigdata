package com.wolf.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.regionserver.BloomType;

import java.io.IOException;

/**
 * @Description HBase数据库表的DDL操作
 * @Author wangqikang
 * @Date 2020-02-22 19:25
 */
public class HBaseClientDDL {

    private static Configuration conf = null;
    private static Connection conn = null;

    /**
     * 参数初始化
     *
     * @throws IOException
     */
    private static void init() throws IOException {
        //构建HBaseConfiguration对象
        conf = HBaseConfiguration.create();
        //设置属性
        //因为HBase客户端读写数据不用经过HMaster，所以只要配置ZooKeeper集群地址
        conf.set("hbase.zookeeper.quorum", "mini1:2181,mini2:2181,mini3:2181");
        //获取连接
        conn = ConnectionFactory.createConnection(conf);
    }

    /**
     * 创建表
     *
     * @throws IOException
     */
    private static void createTable() throws IOException {
        //获取表管理器
        Admin admin = conn.getAdmin();
        //构造表描述器，并指定表名称
        HTableDescriptor htd = new HTableDescriptor(TableName.valueOf("user_info"));
        //构造列族描述器，并指定列族名称
        HColumnDescriptor hcd1 = new HColumnDescriptor("base_info");
        //设置列族的过滤器类型和版本数量
        hcd1.setBloomFilterType(BloomType.ROW).setVersions(1, 3);

        HColumnDescriptor hcd2 = new HColumnDescriptor("extra_info");
        hcd2.setBloomFilterType(BloomType.ROW).setVersions(1, 3);

        //将列族添加到表中
        htd.addFamily(hcd1).addFamily(hcd2);

        admin.createTable(htd);
        admin.close();
    }

    /**
     * 删除表
     *
     * @throws IOException
     */
    private static void dropTable() throws IOException {
        //获取表管理器
        Admin admin = conn.getAdmin();
        //删除表前先禁用表
        admin.disableTable(TableName.valueOf("user_info"));
        admin.deleteTable(TableName.valueOf("user_info"));
        admin.close();
    }

    /**
     * 修改表的schema信息
     *
     * @throws IOException
     */
    private static void modifyTableSchema() throws IOException {
        //获取表管理器
        Admin admin = conn.getAdmin();
        //获取表的描述器
        HTableDescriptor table = admin.getTableDescriptor(TableName.valueOf("user_info"));
        //获取表的列族描述器
        HColumnDescriptor hcd = table.getFamily("base_info".getBytes());
        //修改列族的版本信息
        hcd.setVersions(1, 5);
        //添加新的列族
        table.addFamily(new HColumnDescriptor("other_info"));

        admin.modifyTable(TableName.valueOf("user_info"), table);
        admin.close();
    }


    public static void main(String[] args) throws IOException {
        //参数初始化
        init();

        //建表
        createTable();

        //修改表
        modifyTableSchema();

        //删除表
        dropTable();

        conn.close();
    }
}
