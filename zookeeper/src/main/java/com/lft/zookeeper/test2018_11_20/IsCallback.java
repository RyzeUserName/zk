package com.lft.zookeeper.test2018_11_20;

import org.apache.zookeeper.AsyncCallback;

/**
 * 描述
 * @author Ryze
 * @date 2018/11/20 23:31
 */
public class IsCallback implements AsyncCallback.StringCallback {
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        System.out.println(" 回调 调用中,结果是" + rc + "," + path + "," + ctx + "," + name);
    }
}
