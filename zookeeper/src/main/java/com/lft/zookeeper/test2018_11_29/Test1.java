package com.lft.zookeeper.test2018_11_29;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 *
 *
 */
public class Test1 implements Watcher {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
//        //可注册监听的地方
//        // 1创建连接
//        zooKeeper = new ZooKeeper("192.168.42.128:2181", 5000, new Test1());
//        // 2 getData
//        zooKeeper.setData();
//        zooKeeper.getData();
//        // 3 getChildren
//        zooKeeper.getChildren();
//        // 4 exists
//        zooKeeper.exists();
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
