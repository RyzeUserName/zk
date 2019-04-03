package com.lft.zookeeper.test2018_11_22;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 *
 * 开始使用 curator 操作zookeeper
 *
 * 创建会话
 * 创建节点
 * 删除节点
 * 获取节点信息
 * 更新节点信息
 * 异步操作
 *
 */
public class Test1 {
    public static void main(String[] args) throws Exception {
        String path = "/book-zk";
        String path1 = path + "/c1";
//        //创建会话 普通的
//        CuratorFramework curatorFramework = CuratorFrameworkFactory.
//            newClient("192.168.42.128:2181", 5000, 3000,
//                new ExponentialBackoffRetry(1000, 3));
//        curatorFramework.start();
        //创建会话 函数式
        CuratorFramework build = CuratorFrameworkFactory.builder()
            .connectString("192.168.42.128:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            //.namespace("namespace") //指定命名空间
            .build();
        build.start();
        System.out.println("建立会话");
        //创建节点
        build.create()
            .creatingParentContainersIfNeeded()
            .withMode(CreateMode.EPHEMERAL)
            .forPath(path1, "123".getBytes());
        System.out.println("创建节点");
        Stat stat = new Stat();
        //获取节点信息
        byte[] bytes = build.getData()
            .storingStatIn(stat)
            .forPath(path1);
        System.out.println("获取到节点信息" + new String(bytes));
        //更改信息
        build.setData().withVersion(stat.getVersion()).forPath(path1, "456".getBytes());
        System.out.println("更改信息");

        //获取节点信息
        byte[] bytes1 = build.getData()
            .storingStatIn(stat)
            .forPath(path1);
        System.out.println("获取到节点信息" + new String(bytes1));
        //删除节点
        build.delete().deletingChildrenIfNeeded().forPath(path);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
