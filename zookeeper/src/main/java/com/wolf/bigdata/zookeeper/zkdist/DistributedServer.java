package com.wolf.bigdata.zookeeper.zkdist;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Description 服务器动态上下线
 * @Author wangqikang
 * @Date 2019-08-25 13:54
 */
public class DistributedServer {
    private static final String CONNECT_STRING = "mini1:2181,mini2:2181,mini3:2181";
    private static final int SESSION_TIME_OUT = 2000;
    private static final String PARENT_NODE = "/servers";

    private ZooKeeper zk = null;
    private CountDownLatch latch = new CountDownLatch(1);

    /**
     * 创建zookeeper客户端连接
     *
     * @throws IOException
     */
    private void getConnect() throws IOException, InterruptedException {
        zk = new ZooKeeper(CONNECT_STRING, SESSION_TIME_OUT, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    latch.countDown();
                }

                // 收到事件通知后的回调函数
                // todo list ...
            }
        });
        latch.await();
    }

    /**
     * 向zookeeper集群注册服务器信息
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    private void registerServer(String hostName) throws KeeperException, InterruptedException {
        // 判断父目录是否存在
        Stat exists = zk.exists(PARENT_NODE, false);
        // 如果父目录不存在，则创建
        if (exists == null) {
            zk.create(PARENT_NODE, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

        String path = zk.create(PARENT_NODE + "/server", hostName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(hostName + " is online .. " + path);
    }

    /**
     * 模拟业务功能
     *
     * @param hostName
     * @throws InterruptedException
     */
    private void handleBusiness(String hostName) throws InterruptedException {
        System.out.println(hostName + " start working ... ");
        Thread.sleep(Long.MAX_VALUE);
    }

    public static void main(String[] args) throws Exception {
        DistributedServer server = new DistributedServer();
        // 获取zookeeper连接
        server.getConnect();
        // 向zookeeper注册服务器信息
        server.registerServer(args[0]);
        // 启动业务功能
        server.handleBusiness(args[0]);
    }

}
