package com.lft.zookeeper.test2018_11_21;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 *
 * getData  同步  数据内容或者是版本变更都会触发 watcher
 *
 */
public class Test2 implements Watcher {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper;
    private static Stat stat=new Stat();

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper("192.168.42.128:2181", 5000, new Test2());
        countDownLatch.await();
        //同步创建节点
        String s = zooKeeper.create("/zk-book", "124".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("-----------------"+s);
        System.out.println(new String(zooKeeper.getData("/zk-book",true,stat)));
        System.out.println("--version"+stat.getVersion()+"--Mzxid"+stat.getMzxid()+"--Czxid"+stat.getCzxid());
        zooKeeper.setData("/zk-book","123".getBytes(),-1);
        Thread.sleep(Integer.MAX_VALUE);


    }

    @Override
    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            if (Event.EventType.None == event.getType() && null == event.getPath()) {
                countDownLatch.countDown();
            } else if (Event.EventType.NodeDataChanged == event.getType()) {
                try {
                    System.out.println(new String(zooKeeper.getData("/zk-book",true,stat)));
                    System.out.println("--version"+stat.getVersion()+"--Mzxid"+stat.getMzxid()+"--Czxid"+stat.getCzxid());
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
