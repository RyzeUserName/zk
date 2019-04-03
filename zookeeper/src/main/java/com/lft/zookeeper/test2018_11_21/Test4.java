package com.lft.zookeeper.test2018_11_21;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 *
 * 同步 更新数据  -1 只最新的更新就好
 *
 */
public class Test4 implements Watcher {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper("192.168.42.128:2181", 5000, new Test4());
        countDownLatch.await();

        zooKeeper.create("/zk-book", "124".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zooKeeper.getData("/zk-book", true,null);

        Stat stat = zooKeeper.setData("/zk-book", "456".getBytes(), -1);
        System.out.println("第一次 --version" + stat.getVersion() + "--Mzxid" + stat.getMzxid() + "--Czxid" + stat.getCzxid());

        Stat stat2 = zooKeeper.setData("/zk-book", "456".getBytes(), stat.getVersion());
        System.out.println("第二次 --version" + stat2.getVersion() + "--Mzxid" + stat2.getMzxid() + "--Czxid" + stat2.getCzxid());

        Stat stat3 = zooKeeper.setData("/zk-book", "456".getBytes(), stat.getVersion());
        System.out.println("第三次 --version" + stat3.getVersion() + "--Mzxid" + stat3.getMzxid() + "--Czxid" + stat3.getCzxid());

        zooKeeper.setData("/zk-book", "123".getBytes(), -1);
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
