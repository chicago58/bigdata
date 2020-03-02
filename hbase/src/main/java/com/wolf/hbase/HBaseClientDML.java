package com.wolf.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
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

        ArrayList<Put> puts = new ArrayList<Put>();

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

    /**
     * 批量查询数据
     *
     * @throws IOException
     */
    private static void scanDataFromTable(Filter filter) throws IOException {
        //获取表对象
        Table table = conn.getTable(TableName.valueOf("user_info"));

        Scan scan = new Scan();
        if (filter != null) {
            scan.setFilter(filter);
        }
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

    /**
     * 过滤器查询
     */
    private static void filter() throws IOException {
        //针对Row Key的前缀过滤器
        Filter prefixFilter = new PrefixFilter(Bytes.toBytes("liu"));
        scanDataFromTable(prefixFilter);

        //行过滤器
        RowFilter rowFilter1 = new RowFilter(CompareFilter.CompareOp.LESS, new BinaryComparator(Bytes.toBytes("user002")));
        scanDataFromTable(rowFilter1);
        RowFilter rowFilter2 = new RowFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator("01"));
        scanDataFromTable(rowFilter2);

        //针对指定列的value来过滤
        SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter("base_info".getBytes(), "password".getBytes(), CompareFilter.CompareOp.EQUAL, "123456".getBytes());
        singleColumnValueFilter.setFilterIfMissing(true); //若指定的列缺失，则过滤
        scanDataFromTable(singleColumnValueFilter);

        //针对指定列的value比较器来过滤
        ByteArrayComparable comparator1 = new RegexStringComparator("^zhang");
        ByteArrayComparable comparator2 = new SubstringComparator("ang");
        SingleColumnValueFilter scvf = new SingleColumnValueFilter("base_info".getBytes(), "username".getBytes(), CompareFilter.CompareOp.EQUAL, comparator1);
        scanDataFromTable(scvf);

        //针对列族名的过滤器，返回结果中只会包含满足条件的列族中的数据
        FamilyFilter ff1 = new FamilyFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("info")));
        FamilyFilter ff2 = new FamilyFilter(CompareFilter.CompareOp.EQUAL, new BinaryPrefixComparator(Bytes.toBytes("base")));
        scanDataFromTable(ff2);

        //针对列名的过滤器，返回结果中只会包含满足条件的列数据
        QualifierFilter qf1 = new QualifierFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("password")));
        QualifierFilter qf2 = new QualifierFilter(CompareFilter.CompareOp.EQUAL, new BinaryPrefixComparator(Bytes.toBytes("us")));
        scanDataFromTable(qf1);

        //只返回符合条件的列数据
        ColumnPrefixFilter cf = new ColumnPrefixFilter("pass".getBytes());
        scanDataFromTable(cf);

        //针对多列的前缀过滤器
        byte[][] prefixs = new byte[][]{
                Bytes.toBytes("username"),
                Bytes.toBytes("password")
        };
        MultipleColumnPrefixFilter mcpf = new MultipleColumnPrefixFilter(prefixs);
        scanDataFromTable(mcpf);

        FamilyFilter ff = new FamilyFilter(CompareFilter.CompareOp.EQUAL, new BinaryPrefixComparator(Bytes.toBytes("base")));
        ColumnPrefixFilter cpf = new ColumnPrefixFilter("password".getBytes());
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        filterList.addFilter(ff);
        filterList.addFilter(cpf);
        scanDataFromTable(filterList);
    }

    /**
     * 分页查询
     */
    private void pageScan() throws IOException, InterruptedException {
        //获取表对象
        Table table = conn.getTable(TableName.valueOf("user_info"));

        final byte[] POSTFIX = new byte[]{0x00};
        //设置每页大小
        Filter filter = new PageFilter(3);
        //记录起始行号
        byte[] lastRow = null;
        //记录总数
        int totalRows = 0;
        while (true) {
            Scan scan = new Scan();
            scan.setFilter(filter);
            if (lastRow != null) {
                byte[] startRow = Bytes.add(lastRow, POSTFIX);
                //设置本次查询的起始行
                scan.setStartRow(startRow);
            }

            ResultScanner scanner = table.getScanner(scan);
            int localRows = 0;
            Result result;
            while ((result = scanner.next()) != null) {
                System.out.println(++localRows + ":" + result);
                totalRows++;
                lastRow = result.getRow();
            }
            scanner.close();
            if (localRows == 0) {
                break;
            }
            Thread.sleep(2000);
        }
        System.out.println("total rows: " + totalRows);
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

        //批量查询数据
        scanDataFromTable(null);

        //过滤器查询
        filter();

        conn.close();
    }
}
