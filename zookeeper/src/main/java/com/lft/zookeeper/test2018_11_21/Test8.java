package com.lft.zookeeper.test2018_11_21;

import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.Optional;

/**
 * zkClient handleChildChange
 * 1.可以对不存在的节点监听
 * 2.监听中可以获取最新的子列表
 * 3.可以监听自己的创建、删除
 *
 */

public class Test8 {
    public static void main(String[] args) throws InterruptedException {
        String path="/zk-book";
        ZkClient zkClient = new ZkClient("192.168.42.128:2181", 5000);
        //注册 handleChildChange 监听
        zkClient.subscribeChildChanges(path,(parentPath, currentChilds) -> {
            System.out.println("监听到变化，"+parentPath+"下面发生了变化");
            Optional.ofNullable(currentChilds).ifPresent(b->b.stream().forEach(a-> System.out.println(a)));
        });
        //创建节点
        zkClient.createPersistent(path+"/c1",true);
        Thread.sleep(1000);
        //创建节点
        zkClient.createPersistent(path+"/c2",true);
        Thread.sleep(1000);
        //获取的是子节点的相对路径
        List<String> children = zkClient.getChildren(path);
        children.stream().forEach(a-> System.out.println(a));
        //删除节点
        zkClient.delete(path+"/c1");
        Thread.sleep(1000);
        //删除节点
        zkClient.delete(path+"/c2");
        Thread.sleep(1000);
        //删除节点
        zkClient.delete(path);
        Thread.sleep(Integer.MAX_VALUE);
    }


}
