package com.lft.zookeeper.test2018_11_22;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * 开始使用 curator 操作zookeeper
 *
 * 异步操作
 *
 */
public class Test2 {
    static String path = "/book-zk";
    static String path1 = path + "/c1";
    static CuratorFramework build = CuratorFrameworkFactory.builder()
        .connectString("192.168.42.128:2181")
        .sessionTimeoutMs(5000)
        .retryPolicy(new ExponentialBackoffRetry(1000, 3))
        .build();
    static CountDownLatch countDownLatch = new CountDownLatch(2);
    static ExecutorService executorService = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws Exception {
        build.start();
        System.out.println("建立会话");
        build.create()
            .creatingParentContainersIfNeeded()
            .withMode(CreateMode.EPHEMERAL)
            .inBackground((curatorFramework, curatorEvent) -> {
                countDownLatch.countDown();
                System.out.println("code" + curatorEvent.getResultCode() + "type" + curatorEvent.getType());
                System.out.println("Thread------" + Thread.currentThread());
            },executorService)
            .forPath(path1, "123".getBytes());

        build.create()
            .creatingParentContainersIfNeeded()
            .withMode(CreateMode.EPHEMERAL)
            .inBackground((curatorFramework, curatorEvent) -> {
                countDownLatch.countDown();
                System.out.println("code" + curatorEvent.getResultCode() + "type" + curatorEvent.getType());
                System.out.println("Thread------" + Thread.currentThread());
            })
            .forPath(path1, "123".getBytes());
        countDownLatch.await();
        executorService.shutdown();
    }
}
