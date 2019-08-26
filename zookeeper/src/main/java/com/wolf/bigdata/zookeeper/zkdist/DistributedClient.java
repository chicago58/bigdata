package com.wolf.bigdata.zookeeper.zkdist;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Description 服务器动态上下线
 * @Author wangqikang
 * @Date 2019-08-25 14:28
 */
public class DistributedClient {
    private static final String CONNECT_STRING = "mini1:2181,mini2:2181,mini3:2181";
    private static final int SESSION_TIME_OUT = 2000;
    private static final String PARENT_NODE = "/servers";
    /**
     * 存储在线的服务器主机名
     * 定义类的成员变量servers，由client赋值，防止局部修改，保持数据一致性
     */
    private volatile List<String> servers;
    private ZooKeeper zk;

    private void getConnect() throws Exception {
        zk = new ZooKeeper(CONNECT_STRING, SESSION_TIME_OUT, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                // 收到事件通知后的回调函数
                try {
                    // 更新服务器列表，再次注册监听
                    getServerList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getServerList() throws Exception {
        // 获取服务器子节点信息并监听父节点
        List<String> children = zk.getChildren(PARENT_NODE, true);
        if (Objects.nonNull(children)) {
            List<String> servers = new ArrayList<>();
            for (String child : children) {
                byte[] data = zk.getData(PARENT_NODE + "/" + child, false, null);
                servers.add(new String(data));
            }

            this.servers = servers;
            System.out.println(this.servers);
        }
    }

    /**
     * 业务功能
     *
     * @throws InterruptedException
     */
    private void handleBusiness() throws InterruptedException {
        System.out.println("client start working ...");
        Thread.sleep(Long.MAX_VALUE);
    }

    public static void main(String[] args) throws Exception {
        DistributedClient client = new DistributedClient();
        // 获取zookeeper客户端连接
        client.getConnect();
        // 获取服务器的列表并监听
        client.getServerList();
        // 启动业务线程
        client.handleBusiness();
    }
}
