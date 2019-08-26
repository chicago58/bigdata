package com.wolf.bigdata.zookeeper.zkclient;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * @Description
 * @Author wangqikang
 * @Date 2019-08-23 7:57
 */
public class SimpleDemo {
    // 设置会话超时时间
    private static final int SESSION_TIMEOUT = 30000;
    // 创建 ZooKeeper 实例
    ZooKeeper zk;
    // 创建 Watcher 实例
    Watcher watcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            System.out.println(event);
        }
    };

    // 初始化 ZooKeeper 实例
    private void createZkInstance() throws IOException {
        zk = new ZooKeeper("mini1:2181,mini2:2181,mini3:2181", SESSION_TIMEOUT, watcher);
    }

    private void zkOperations() throws KeeperException, InterruptedException {
        System.out.println("\n1.创建 ZooKeeper 节点（znode:zoo2,data:myData2,acl:OPEN_ACL_UNSAFE,mode:PERSISTENT）");
        zk.create("/zoo2", "myData2".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        System.out.println("\n2.查看节点是否创建成功");
        System.out.println(new String(zk.getData("/zoo2", false, null)));

        System.out.println("\n3.修改节点数据");
        zk.setData("/zoo2", "shenlan1234".getBytes(), -1);

        System.out.println("\n4.查看节点数据是否修改成功");
        System.out.println(new String(zk.getData("/zoo2", false, null)));

        System.out.println("\n5.删除节点");
        zk.delete("/zoo2", -1);

        System.out.println("\n6.查看节点是否被删除");
        System.out.println("节点状态：" + zk.exists("/zoo2", false));
    }

    private void zkClose() throws InterruptedException {
        zk.close();
    }

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        SimpleDemo demo = new SimpleDemo();
        demo.createZkInstance();
        demo.zkOperations();
        demo.zkClose();
    }
}
