package com.wolf.bigdata.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Description
 * @Author wangqikang
 * @Date 2019-08-22 7:23
 */
@Ignore
public class SimpleZkClientTest {
    private static final String connectString = "mini1:2181,mini2:2181,mini3:2181";
    private static final int sessionTimeout = 2000;
    ZooKeeper zkClient = null;
    /**
     * latch相当于对象锁，latch.await()方法执行时会阻塞方法所在的线程
     * 当latch的count减为0时，唤醒阻塞的线程
     * 防止操作zookeeper时，客户端连接没有创建完成导致报错
     */
    CountDownLatch latch = new CountDownLatch(1);

    /**
     * 创建客户端连接
     *
     * @throws Exception
     */
    @Before
    public void init() throws Exception {
        /**
         * 创建客户端连接时与zookeeper集群三次握手，而握手的过程并不是在一瞬间完成；
         * 当主线程中创建对象的语句执行完成并调用zookeeper API创建节点时，也许客户端没有完成连接（可能报错）；
         * 此时应该等待客户端连接对象创建完成。
         *
         * 而客户端连接对象创建完成后会调用回调方法，传入的事件为SyncConnect（同步连接）
         */
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            /**
             * 回调方法（事件处理逻辑）
             * 1.客户端创建连接成功会调用回调方法
             * 2.当客户端收到监听的事件通知时调用回调方法
             */
            @Override
            public void process(WatchedEvent event) {
                if (latch.getCount() > 0 && event.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println("countdown");
                    latch.countDown();
                }

                // 收到事件通知后的回调逻辑
                System.out.println(event.getType() + "---" + event.getPath());
                System.out.println(event.getState());
            }
        });
        latch.await();
    }

    /**
     * 创建节点
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void testCreate() throws KeeperException, InterruptedException {
        /**
         * 参数1：要创建节点的路径
         * 参数2：节点的数据，上传的数据可以是任意类型，但都要转换成byte[]
         * 参数3：节点的权限
         * 参数4：节点的类型
         */
        String nodeCreated = zkClient.create("/hadoop", "zookeeper".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zkClient.close();
    }

    /**
     * 判断节点是否存在
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void testExist() throws KeeperException, InterruptedException {
        Stat stat = zkClient.exists("/hadoop", false);
        System.out.println(stat == null ? "not exist" : "exist");
    }

    /**
     * 获取子节点
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void getChildren() throws KeeperException, InterruptedException {
        List<String> children = zkClient.getChildren("/hadoop", true);
        for (String child : children) {
            System.out.println(child);
        }
        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * 获取节点数据
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void getData() throws KeeperException, InterruptedException {
        byte[] data = zkClient.getData("/hadoop", true, null);
        System.out.println(new String(data));
        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * 删除节点
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void deleteZnode() throws KeeperException, InterruptedException {
        // 指定要删除的版本，-1表示删除所有版本
        zkClient.delete("/eclipse", -1);
    }

    /**
     * 修改节点数据
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void setData() throws KeeperException, InterruptedException {
        zkClient.setData("/hadoop", "i miss you".getBytes(), -1);
        byte[] data = zkClient.getData("/hadoop", false, null);
        System.out.println(new String(data));
    }
}
