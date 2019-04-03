package com.lft.zookeeper.test2018_11_21;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 *
 * 异步 更新数据  -1 只最新的更新就好
 *
 */
public class Test5 implements Watcher {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper("192.168.42.128:2181", 5000, new Test5());
        countDownLatch.await();
        zooKeeper.create("/zk-book", "124".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zooKeeper.getData("/zk-book", true, null);
        zooKeeper.setData("/zk-book", "456".getBytes(), -1, new MyStatCallback(), null);
        zooKeeper.setData("/zk-book", "789".getBytes(), -1, new MyStatCallback(), null);
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Override
    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            if (Event.EventType.None == event.getType() && null == event.getPath()) {
                countDownLatch.countDown();
            }
        }
    }
}
