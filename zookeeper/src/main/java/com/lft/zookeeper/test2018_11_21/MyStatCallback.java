package com.lft.zookeeper.test2018_11_21;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.data.Stat;

public class MyStatCallback implements AsyncCallback.StatCallback {
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        System.out.println("回调生效中--" + rc + "," + path + "," + ctx);
        System.out.println("回调生效中--" + stat.getCzxid() + "," + stat.getMzxid() + "," + stat.getVersion());
    }
}
