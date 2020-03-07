package com.wolf;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.*;
import java.net.URI;


/**
 * @Description 流的方式操作HDFS
 * @Author wangqikang
 * @Date 2020-03-07 15:21
 */
public class StreamAccessHdfs {
    private static FileSystem fs = null;

    public static void main(String[] args) throws Exception {
        init();

        //HDFS文件上传
//        uploadToHDFS();

        //HDFS文件下载
//        downloadToLocal();

        //HDFS随机访问，指定起始偏移量和读取长度
//        randomAccess();

        //HDFS文件读取
//        readFile();

        //读取指定block
        readBlock();
    }

    /**
     * 读取指定block
     *
     * @throws Exception
     */
    private static void readBlock() throws Exception {
        FSDataInputStream in = fs.open(new Path("/person.txt"));
        //获取文件信息
        FileStatus[] listStatus = fs.listStatus(new Path("/person.txt"));
        //获取文件所有block信息
        BlockLocation[] fileBlockLocations = fs.getFileBlockLocations(listStatus[0], 0L, listStatus[0].getLen());

        //第一个block的长度
        long length = fileBlockLocations[0].getLength();
        //第一个block的起始偏移量
        long offset = fileBlockLocations[0].getOffset();

        System.out.println(length);
        System.out.println(offset);

        //获取第一个block并写入输出流
//		IOUtils.copyBytes(in, System.out, (int)length);
        byte[] b = new byte[4096];

        FileOutputStream os = new FileOutputStream(new File("F:\\block0.txt"));
        while (in.read(offset, b, 0, 4096) != -1) {
            os.write(b);
            offset += 4096;
            if (offset > length) return;
        }

        os.flush();
        os.close();
        in.close();
    }

    /**
     * 读取HDFS文件内容
     *
     * @throws IOException
     */
    private static void readFile() throws IOException {
        FSDataInputStream in = fs.open(new Path("/person.txt"));

        org.apache.hadoop.io.IOUtils.copyBytes(in, System.out, 1024);
    }

    /**
     * HDFS支持随机定位的文件读取，而且可以读取指定长度
     *
     * @throws Exception
     */
    private static void randomAccess() throws Exception {
        //先获取文件输入流（HDFS）
        FSDataInputStream in = fs.open(new Path("/person.txt"));

        //自定义流的起始偏移量
        in.seek(1);

        //再构造文件输出流（本地）
        FileOutputStream out = new FileOutputStream(new File("F:\\temp.txt"));

        org.apache.hadoop.io.IOUtils.copyBytes(in, out, 19L, true);
    }

    private static void downloadToLocal() throws Exception {
        //先获取文件输入流（HDFS）
        FSDataInputStream in = fs.open(new Path("/person.txt"));

        //再构造文件输出流（本地）
        FileOutputStream out = new FileOutputStream(new File("F:\\3.txt"));

        //将输入流中数据传输到输出流
        org.apache.hadoop.io.IOUtils.copyBytes(in, out, 4096);
    }

    /**
     * 流实现HDFS文件上传
     *
     * @throws Exception
     */
    private static void uploadToHDFS() throws Exception {
        // 获取文件输出流（HDFS）
        FSDataOutputStream outputStream = fs.create(new Path("/1.txt"), true);
        // 获取文件输入流（本地）
        FileInputStream inputStream = new FileInputStream("F:\\3.txt");

        IOUtils.copy(inputStream, outputStream);
    }

    private static void init() throws Exception {
        Configuration conf = new Configuration();
        fs = FileSystem.get(new URI("hdfs://mini1:9000"), conf, "root");
    }
}
