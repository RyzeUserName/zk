package com.lft.zookeeper.test2018_11_20;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class Test2 implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.42.128:2181", 5000, new Test2());
        System.out.println(zooKeeper.getState());
        countDownLatch.await();
        System.out.println("zookeeper session is established");
        long sessionId = zooKeeper.getSessionId();
        byte[] sessionPasswd = zooKeeper.getSessionPasswd();
        //错误连接
        zooKeeper = new ZooKeeper("192.168.42.128:2181", 5000, new Test2(), 1L, "test".getBytes());
        //正确的
        zooKeeper = new ZooKeeper("192.168.42.128:2181", 5000, new Test2(), sessionId, sessionPasswd);
        Thread.sleep(Integer.MAX_VALUE);

    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println(" receive watch event " + event);
        if (Event.KeeperState.SyncConnected == event.getState()) {
            countDownLatch.countDown();
        }
    }
}
