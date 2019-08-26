package com.wolf.bigdata.zookeeper.zklock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @Description 分布式共享锁
 * @Author wangqikang
 * @Date 2019-08-21 22:43
 */
public class DistributedClientLock {
    private String HOSTS = "mini1:2181,mini2:2181,mini3:2181";
    private static final int SESSION_TIMEOUT = 2000;
    private String GROUP_NODE = "locks";
    private String SUB_NODE = "sub";

    CountDownLatch latch = new CountDownLatch(1);

    private ZooKeeper zk;
    private volatile String thisPath;

    private void connectZookeeper() throws Exception {
        zk = new ZooKeeper(HOSTS, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (latch.getCount() > 0 && event.getState() == Event.KeeperState.SyncConnected) {
                    latch.countDown();
                }

                try {
                    // 判断事件类型，只处理子节点变化事件
                    if (event.getType() == Event.EventType.NodeChildrenChanged && event.getPath().equals("/" + GROUP_NODE)) {
                        // 获取子节点，并监听父节点
                        List<String> children = zk.getChildren("/" + GROUP_NODE, true);
                        String thisNode = thisPath.substring(("/" + GROUP_NODE + "/").length());
                        // 比较是否最小
                        Collections.sort(children);
                        if (children.indexOf(thisNode) == 0) {
                            // 访问共享资源处理业务，并在处理完成后删除锁
                            doSomething();

                            // 重新注册一把锁
                            thisPath = zk.create("/" + GROUP_NODE + "/" + SUB_NODE, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        latch.await();

        // 判断父目录是否存在
        Stat exists = zk.exists("/" + GROUP_NODE, null);
        // 如果不存在，则创建
        if (exists == null) {
            zk.create("/" + GROUP_NODE, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

        // 向zookeeper注册一把锁
        thisPath = zk.create("/" + GROUP_NODE + "/" + SUB_NODE, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        Thread.sleep(new Random().nextInt(1000));

        // 获取zookeeper父目录下所有子节点，并监听父节点
        List<String> childrenNodes = zk.getChildren("/" + GROUP_NODE, true);
        // 只有一个子节点
        if (Objects.nonNull(childrenNodes) && childrenNodes.size() == 1) {
            doSomething();
            thisPath = zk.create("/" + GROUP_NODE + "/" + SUB_NODE, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        }
    }

    /**
     * 处理业务逻辑，并释放锁
     *
     * @throws Exception
     */
    private void doSomething() throws Exception {
        try {
            System.out.println("gain lock: " + thisPath);
            Thread.sleep(2000);

            // todo something
        } finally {
            System.out.println("finished: " + thisPath);
            // 访问完成后需要手动删除锁节点
            zk.delete(thisPath, -1);
        }
    }

    public static void main(String[] args) throws Exception {
        DistributedClientLock dl = new DistributedClientLock();
        dl.connectZookeeper();
        Thread.sleep(Long.MAX_VALUE);
    }

}
