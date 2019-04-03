package com.lft.zookeeper.test2018_11_23;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

/**
 * 分布式 计数器  并不是在data 里
 */
public class Test1 {

    static String path = "/book-zk";
    static CuratorFramework build = CuratorFrameworkFactory.builder()
        .connectString("192.168.42.128:2181")
        .sessionTimeoutMs(50000)
        .retryPolicy(new ExponentialBackoffRetry(1000, 3))
        .build();

    public static void main(String[] args) throws Exception {

        build.start();
        DistributedAtomicInteger distributedAtomicInteger = new DistributedAtomicInteger(build, path, new RetryNTimes(3, 1000));

        AtomicValue<Integer> add = distributedAtomicInteger.add(8);
        System.out.println(add.succeeded());
        System.out.println(add.postValue());

        AtomicValue<Integer> add1 = distributedAtomicInteger.add(8);
        System.out.println(add1.succeeded());
        System.out.println(add1.postValue());
    }
}
