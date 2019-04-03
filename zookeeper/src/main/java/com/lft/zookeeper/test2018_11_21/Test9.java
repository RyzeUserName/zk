package com.lft.zookeeper.test2018_11_21;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.Optional;

/**
 * zkClient 数据获取 readData
 *
 */

public class Test9 {
    public static void main(String[] args) throws InterruptedException {
        String path = "/zk-book";
        ZkClient zkClient = new ZkClient("192.168.42.128:2181", 5000);
        //注册 IZkDataListener 监听
        zkClient.subscribeDataChanges(path, new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println(dataPath + "数据变更 监听到" + data);
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println(dataPath + "数据删除 监听到");

            }
        });
        //创建节点
        zkClient.createPersistent(path , true);
        Thread.sleep(1000);
        //修改节点
        zkClient.writeData(path,"修改内容");
        Thread.sleep(1000);
        //删除节点
        zkClient.delete(path);
        Thread.sleep(Integer.MAX_VALUE);
    }


}
