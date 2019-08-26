package com.wolf.bigdata.zookeeper.zkclient;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Description
 * @Author wangqikang
 * @Date 2019-08-22 8:08
 */
public class ZkClient {
    static ZooKeeper zk = null;

    public static void main(String[] args) throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        zk = new ZooKeeper("mini1:2181,mini2:2181,mini3:2181", 2000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    latch.countDown();
                }

                System.out.println(event.getPath());
                System.out.println(event.getType());

                // 若要持续监听，则在响应监听时再次注册监听事件
                try {
//                    zk.getData("/mygirlfriends", true, null);
                    zk.getChildren("/mygirlfriends", true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        latch.await();

        // 创建节点
//        zk.create("/mygirlfriends", "李思佳".getBytes("UTF-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        zk.create("/mygirlfriends/lisijia", "狮子座".getBytes("UTF-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
//        zk.create("/mygirlfriends/xuyuting", "处女座".getBytes("UTF-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
//        zk.create("/mygirlfriends/lisijia", "狮子座".getBytes("UTF-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
//        zk.close();

        // 获取节点数据
//        byte[] data = zk.getData("/mygirlfriends", true, null);
//        System.out.println(new String(data, "UTF-8"));
        // 程序主线程休眠，等待监听事件
//        Thread.sleep(Long.MAX_VALUE);

        // 获取子节点
//        List<String> children = zk.getChildren("/mygirlfriends", true);
//        for (String child : children) {
//            System.out.println(child);
//        }
//        Thread.sleep(Long.MAX_VALUE);

        // 删除节点
//        zk.delete("/mygirlfriends/jiajia", -1);
//        zk.close();

        // 修改节点数据
//        zk.setData("/mygirlfriends", "哈哈".getBytes("UTF-8"), -1);
//        zk.close();

        // 判断节点是否存在
//        Stat exists = zk.exists("/mywife", true);
//        System.out.println(exists == null ? "节点不存在" : "节点存在");
    }
}
