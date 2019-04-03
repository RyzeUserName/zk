package com.lft.zookeeper.test2018_11_22;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;

/**
 *
 * 应用场景:
 * 事件监听
 * Master 选举
 * 分布式锁
 * 分布式计数器
 * 分布式barrier
 *
 */
public class Test3 {

    static String path = "/book-zk";
    static String path1 = path + "/c1";
    static CountDownLatch countDownLatch=new CountDownLatch(1);
    public static void main(String[] args) throws Exception {
        //监听变化
        //pathChange();

        //监听一级子节点变化
       pathChildChange();

//        //选举 select
//        for (Integer i=0;i<5;i++){
//            Integer finalI = i;
//            new Thread(()->{
//                try {
//                    countDownLatch.await();
//                    select(finalI);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }).start();
//        }
//        countDownLatch.countDown();
        //分布式锁
        //普通订单
//        orderNumber();
//        //分布式锁
//        orderNumberZk();
    }

    static CuratorFramework build = CuratorFrameworkFactory.builder()
        .connectString("192.168.42.128:2181")
        .sessionTimeoutMs(50000)
        .retryPolicy(new ExponentialBackoffRetry(1000, 3))
        .build();

    public static void pathChange() throws Exception {
        build.start();
        build.create().creatingParentsIfNeeded()
            .withMode(CreateMode.EPHEMERAL)
            .forPath(path, "123".getBytes());
        NodeCache nodeCache = new NodeCache(build, path, false);
        nodeCache.start();
        nodeCache.getListenable().addListener(() ->
            System.out.println("数据是" + new String(nodeCache.getCurrentData().getData())));
        build.setData().forPath(path, "456".getBytes());
        Thread.sleep(1000);
        build.delete().deletingChildrenIfNeeded().forPath(path);
        Thread.sleep(Integer.MAX_VALUE);
    }

    public static void pathChildChange() throws Exception {
        build.start();
        PathChildrenCache pathChildrenCache = new PathChildrenCache(build, path, true);
        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        pathChildrenCache.getListenable().addListener((curatorFramework, pathChildrenCacheEvent) -> {
            switch (pathChildrenCacheEvent.getType()) {
                case CHILD_ADDED:
                    System.out.println("child add" + pathChildrenCacheEvent.getData().getPath());
                    break;
                case CHILD_UPDATED:
                    System.out.println("child update" + pathChildrenCacheEvent.getData().getPath());
                    break;
                case CHILD_REMOVED:
                    System.out.println("child remove" + pathChildrenCacheEvent.getData().getPath());
                    break;

            }
        });
        build.create().creatingParentsIfNeeded()
            .withMode(CreateMode.PERSISTENT)
            .forPath(path, "123".getBytes());
        Thread.sleep(1000);
        build.create().creatingParentsIfNeeded()
            .withMode(CreateMode.EPHEMERAL)
            .forPath(path1, "456".getBytes());
        Thread.sleep(1000);
        build.setData().forPath(path1, "789".getBytes());
        Thread.sleep(1000);
        build.delete().forPath(path1);
        Thread.sleep(Integer.MAX_VALUE);
    }

    public static void select(int id) throws Exception {
        build.start();
        LeaderSelector selector = new LeaderSelector(build, path, new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                System.out.println("成为master "+id);
                Thread.sleep(Integer.MAX_VALUE);
            }
        });
        selector.autoRequeue();
        selector.start();
        Thread.sleep(Integer.MAX_VALUE);
    }

    public static void orderNumber() throws Exception {
        for (int i=0;i<10;i++){
            new Thread(()->{
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("生成订单号"+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss|SSS")));
            }).start();
        }
        countDownLatch.countDown();
    }

    public static void orderNumberZk() throws Exception {
        build.start();
        InterProcessMutex interProcessLock = new InterProcessMutex(build,path);
        for (int i=0;i<10;i++){
            new Thread(()->{
                try {
                    countDownLatch.await();
                    interProcessLock.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("生成订单号"+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss|SSS")));

                try {
                    interProcessLock.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        countDownLatch.countDown();
    }


}
