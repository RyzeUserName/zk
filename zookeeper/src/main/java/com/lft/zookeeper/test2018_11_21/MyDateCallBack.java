package com.lft.zookeeper.test2018_11_21;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.data.Stat;

/**
 * 描述
 * @author Ryze
 * @date 2018/11/21 21:48
 */
public class MyDateCallBack implements AsyncCallback.DataCallback {
    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        System.out.println("回调生效中--"+rc + ","+path +","+new String(data)+","+ctx);
        System.out.println("回调生效中--"+stat.getCzxid() + ","+stat.getMzxid() +","+stat.getVersion());
    }
}
