package com.wolf.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @Description HBase数据库表的DML操作
 * @Author wangqikang
 * @Date 2020-02-22 22:02
 */
public class HBaseClientDML {
    private static Configuration conf;
    private static Connection conn;

    /**
     * 初始化参数
     *
     * @throws IOException
     */
    private static void init() throws IOException {
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "mini1:2181,mini2:2181,mini3:2181");
        conn = ConnectionFactory.createConnection(conf);
    }

    /**
     * 插入数据
     *
     * @throws IOException
     */
    private static void putDataToTable() throws IOException {
        //获取表对象
        Table table = conn.getTable(TableName.valueOf("user_info"));

        ArrayList<Put> puts = new ArrayList<>();

        //构建Put对象，并指定Row Key
        Put put1 = new Put(Bytes.toBytes("user001"));
        put1.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("username"), Bytes.toBytes("zhangsan"));

        Put put2 = new Put("user001".getBytes());
        put2.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("password"), Bytes.toBytes("123456"));

        Put put3 = new Put("user002".getBytes());
        put3.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("username"), Bytes.toBytes("lisi"));
        put3.addColumn(Bytes.toBytes("extra_info"), Bytes.toBytes("married"), Bytes.toBytes("false"));

        Put put4 = new Put("zhang_sh_01".getBytes());
        put4.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("username"), Bytes.toBytes("zhang01"));
        put4.addColumn(Bytes.toBytes("extra_info"), Bytes.toBytes("married"), Bytes.toBytes("false"));

        puts.add(put1);
        puts.add(put2);
        puts.add(put3);
        puts.add(put4);

        table.put(puts);
        table.close();
    }

    /**
     * 获取数据
     * get方式每次只能读取一行数据
     *
     * @throws IOException
     */
    private static void getDataFromTable() throws IOException {
        //获取表对象
        Table table = conn.getTable(TableName.valueOf("user_info"));

        //构造get查询对象
        Get getObj = new Get("user001".getBytes());
        Result result = table.get(getObj);
        CellScanner cellScanner = result.cellScanner();
        while (cellScanner.advance()) {
            Cell current = cellScanner.current();
            //获取列族
            byte[] familyArray = current.getFamilyArray();
            //获取列
            byte[] qualifierArray = current.getQualifierArray();
            //获取值
            byte[] valueArray = current.getValueArray();

            System.out.println(new String(familyArray));
            System.out.println(new String(qualifierArray));
            System.out.println(new String(valueArray));

            System.out.print(new String(familyArray, current.getFamilyOffset(), current.getFamilyLength()));
            System.out.print(":" + new String(qualifierArray, current.getQualifierOffset(), current.getFamilyLength()));
            System.out.println(" " + new String(valueArray, current.getValueOffset(), current.getValueLength()));

            System.out.println("---------------------------------");
        }

        table.close();
    }

    /**
     * 删除数据
     *
     * @throws IOException
     */
    private static void deleteDataFromTable() throws IOException {
        //获取表对象
        Table table = conn.getTable(TableName.valueOf("user_info"));

        Delete deleteObj = new Delete("user001".getBytes());
        deleteObj.addColumn("base_info".getBytes(), "password".getBytes());
        table.delete(deleteObj);
        table.close();
    }

    private static void scanDataFromTable() throws IOException {
        //获取表对象
        Table table = conn.getTable(TableName.valueOf("user_info"));

        Scan scan = new Scan();
        ResultScanner scanner = table.getScanner(scan);
        Iterator<Result> iterator = scanner.iterator();
        while (iterator.hasNext()) {
            Result result = iterator.next();
            CellScanner cellScanner = result.cellScanner();

            while (cellScanner.advance()) {
                Cell current = cellScanner.current();
                byte[] familyArray = current.getFamilyArray();
                byte[] rowArray = current.getRowArray();
                byte[] qualifierArray = current.getQualifierArray();
                byte[] valueArray = current.getValueArray();

                System.out.println(new String(rowArray, current.getRowOffset(), current.getRowLength()));
                System.out.print(new String(familyArray, current.getFamilyOffset(), current.getFamilyLength()));
                System.out.print(":" + new String(qualifierArray, current.getQualifierOffset(), current.getQualifierLength()));
                System.out.println(" " + new String(valueArray, current.getValueOffset(), current.getValueLength()));
            }

            System.out.println("-------------------");
        }

        table.close();
    }

    public static void main(String[] args) throws IOException {
        //初始化参数
        init();

        //插入数据
//        putDataToTable();

        //删除数据
//        deleteDataFromTable();

        //获取数据
        getDataFromTable();

        //全表扫描
        scanDataFromTable();

        conn.close();
    }
}
