package com.wolf.hadoop.rpc.client;

import com.wolf.hadoop.rpc.protocol.ClientNameNodeProtocol;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @Description
 * @Author wangqikang
 * @Date 2019-08-08 07:20
 */
public class HdfsClient {

    public static void main(String[] args) throws IOException {
        // 获取动态代理对象
        ClientNameNodeProtocol proxy = RPC.getProxy(ClientNameNodeProtocol.class, 1L, new InetSocketAddress("localhost", 8787), new Configuration());

        String metaData = proxy.getMetaData("/a.doc");
        System.out.println(metaData);
    }
}
