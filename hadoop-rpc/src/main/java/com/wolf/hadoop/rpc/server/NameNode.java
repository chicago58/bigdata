package com.wolf.hadoop.rpc.server;

import com.wolf.hadoop.rpc.protocol.ClientNameNodeProtocol;

/**
 * @Description
 * @Author wangqikang
 * @Date 2019-08-06 08:42
 */
public class NameNode implements ClientNameNodeProtocol {

    public String getMetaData(String path) {
        return path + " 2 {BLK1, BLK2} {BLK1: mini1, mini2} {BLK2: mini4, mini5}";
    }
}
