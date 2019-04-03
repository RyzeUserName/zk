package com.lft.zookeeper.test2018_11_23;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.EnsurePath;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * zkPath 构建节点
 * EnsurePath  确认存在
 * TestingServer  单机测试
 * TestingCluster   集群测试
 */
public class Test3 {
    static String path = "/zk-test";
    static CuratorFramework build = CuratorFrameworkFactory.builder()
        .connectString("192.168.42.128:2181")
        .sessionTimeoutMs(50000)
        .retryPolicy(new ExponentialBackoffRetry(1000, 3))
        .build();

    public static void main(String[] args) throws Exception {
        //testZkPath();

        //testEnSurePath();

    }

    public static void testZkPath() throws Exception {
        build.start();
        ZooKeeper zooKeeper = build.getZookeeperClient().getZooKeeper();

        System.out.println(ZKPaths.fixForNamespace("sub", path));
        System.out.println(ZKPaths.makePath(path, "sub"));
        System.out.println(ZKPaths.getNodeFromPath(path + "/sub"));

        ZKPaths.PathAndNode pathAndNode = ZKPaths.getPathAndNode(path + "/sub1");
        System.out.println(pathAndNode.getNode());
        System.out.println(pathAndNode.getPath());

        String child1 = path + "/child1";
        String child2 = path + "/child2";
        ZKPaths.mkdirs(zooKeeper, child1);
        ZKPaths.mkdirs(zooKeeper, child2);

        System.out.println(ZKPaths.getSortedChildren(zooKeeper, path));

        ZKPaths.deleteChildren(build.getZookeeperClient().getZooKeeper(), path, true);

    }

    public static void testEnSurePath() throws Exception {
        build.start();

        build.usingNamespace("zk-book");
        EnsurePath ensurePath = new EnsurePath(path);

        ensurePath.ensure(build.getZookeeperClient());
        ensurePath.ensure(build.getZookeeperClient());

        EnsurePath ensurePath1 = build.newNamespaceAwareEnsurePath("/c1");
        ensurePath1.ensure(build.getZookeeperClient());

    }
    public static void testingServer(){
    }


}
