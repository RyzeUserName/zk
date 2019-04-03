package com.lft.zookeeper.test2018_11_20;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 不允许 递归创建节点
 * 只允许 删除叶子节点
 */
public class Test3 implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.42.128:2181", 5000, new Test3());
        countDownLatch.await();
//        //同步创建节点  不受权限控制 临时节点
//        String s = zooKeeper.create("/zk-book1", "内容".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//        //  /zk-book1
//        System.out.println("path ----------------" + s);
//
//        //同步创建节点 不受权限控制 临时序列节点
//        String s1 = zooKeeper.create("/zk-book2", "内容".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
//        // /zk-book20000000002
//        System.out.println("path ----------------" + s1);
//
//        //异步创建节点  不受权限控制 临时节点
//        zooKeeper.create("/zk-book3", "内容".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
//            CreateMode.EPHEMERAL,new IsCallback(),"上下文");
//
//        //异步创建节点  不受权限控制 临时序列节点
//        zooKeeper.create("/zk-book4", "内容".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
//            CreateMode.EPHEMERAL_SEQUENTIAL,new IsCallback(),"上下文");

//         Thread.sleep(Integer.MAX_VALUE);


//        //节点创建
//        String s = zooKeeper.create("/zk-delete", "内容".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        System.out.println(s);
    }

    @Override
    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            countDownLatch.countDown();
        }
    }
}
