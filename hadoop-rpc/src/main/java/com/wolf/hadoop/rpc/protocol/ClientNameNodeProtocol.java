package com.wolf.hadoop.rpc.protocol;

/**
 * @Description
 * @Author wangqikang
 * @Date 2019-08-08 07:14
 */
public interface ClientNameNodeProtocol {

    long versionID = 1L;

    String getMetaData(String path);
}
