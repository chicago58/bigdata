package com.wolf.hadoop.rpc.server;

import com.wolf.hadoop.rpc.protocol.ClientNameNodeProtocol;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.Server;

import java.io.IOException;

/**
 * @Description
 * @Author wangqikang
 * @Date 2019-08-07 22:14
 */
public class ServerPublisher {

    public static void main(String[] args) throws IOException {
        RPC.Builder builder = new RPC.Builder(new Configuration());

        builder.setBindAddress("localhost").setPort(8787)
                .setProtocol(ClientNameNodeProtocol.class)
                .setInstance(new NameNode());

        Server server = builder.build();
        server.start();
    }
}
