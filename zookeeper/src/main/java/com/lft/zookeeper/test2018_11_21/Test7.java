package com.lft.zookeeper.test2018_11_21;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * 权限控制
 *
 *
 */
public class Test7 {
    private static String PATH = "/zk-book-auth";
    private static String PATH2 = "/zk-book-auth/test2";
    private static String PATH3 = "/zk-book-auth/test3";

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.42.128:2181", 5000, event -> { });
        zooKeeper.addAuthInfo("digest", "foo:true".getBytes());
        zooKeeper.create(PATH, "123".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        zooKeeper.create(PATH2, "456".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);
        zooKeeper.create(PATH3, "789".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);
//        // NoAuth for /zk-book-auth
//        ZooKeeper zooKeeper2 = new ZooKeeper("192.168.42.128:2181", 5000, null);
//        zooKeeper2.getData(PATH,false,null);

//        // NoAuth for /zk-book-auth
//        ZooKeeper zooKeeper2 = new ZooKeeper("192.168.42.128:2181", 5000, null);
//        zooKeeper2.addAuthInfo("digest", "foo:false".getBytes());
//        zooKeeper2.getData(PATH, false, null);

        //删除节点 有点特殊 创建的节点没权限也可以删除 但是其下面的子节点是删不掉的

        ZooKeeper zooKeeper2 = new ZooKeeper("192.168.42.128:2181", 5000, event -> { });
        ZooKeeper zooKeeper3 = new ZooKeeper("192.168.42.128:2181", 5000, event -> { });
        ZooKeeper zooKeeper4 = new ZooKeeper("192.168.42.128:2181", 5000, event -> { });
        // Directory not empty for /zk-book-auth 也就是说没权限也可删除 但是非空不能删除
        //zooKeeper2.delete(PATH,-1);

        zooKeeper3.addAuthInfo("digest", "foo:true".getBytes());
        zooKeeper4.addAuthInfo("digest", "foo:true".getBytes());
        zooKeeper3.delete(PATH2,-1);
        zooKeeper4.delete(PATH3,-1);

        zooKeeper2.delete(PATH,-1);

    }

}