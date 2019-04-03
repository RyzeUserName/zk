package com.lft.zookeeper.test2018_11_21;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.data.Stat;

import java.util.List;

public class IChildren2Callback implements AsyncCallback.Children2Callback {
    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
        System.out.println("rc:" + rc + ",path:" + path + ",ctx:" + ctx + ",childrenList:" + children + ",stat:" + stat);
    }
}
