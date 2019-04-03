package com.lft.zookeeper.test2018_11_23;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Random;
import java.util.concurrent.*;

/**
 * barrier
 *
 * 线程屏障
 * 类似于一个 线程等待带某个情况
 *
 *
 */
public class Test2 {
    public static void main(String[] args) throws Exception {
        // jdk
        //jdkBarrier();


        //zk 感觉不好
        //zkBarrier();
        zkBarrier2();

    }

    public static void jdkBarrier() {
        // 三个参赛选手
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Runnable runnable = () -> {
            System.out.println("准备好了" + Thread.currentThread() + System.currentTimeMillis());
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println("开始跑了" + Thread.currentThread() + " 时间" + System.currentTimeMillis());
        };
        executorService.submit(runnable);
        executorService.submit(runnable);
        executorService.submit(runnable);
        executorService.shutdown();


    }

    public static void zkBarrier() throws Exception {
        String path = "/book-zk";
        final DistributedBarrier[] distributedBarrier = new DistributedBarrier[1];
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(() -> {
                CuratorFramework build = CuratorFrameworkFactory.builder()
                    .connectString("192.168.42.128:2181")
                    .sessionTimeoutMs(50000)
                    .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                    .build();
                build.start();
                System.out.println(finalI + "准备");
                distributedBarrier[0] = new DistributedBarrier(build, path);
                try {
                    distributedBarrier[0].setBarrier();
                    distributedBarrier[0].waitOnBarrier();
                    System.out.println(finalI + "启动");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        Thread.sleep(20000);
        distributedBarrier[0].removeBarrier();
    }

    public static void zkBarrier2() throws Exception {
        String path = "/book-zk1";
        final DistributedDoubleBarrier[] distributedBarrier = new DistributedDoubleBarrier[1];
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(() -> {
                CuratorFramework build = CuratorFrameworkFactory.builder()
                    .connectString("192.168.42.128:2181")
                    .sessionTimeoutMs(50000)
                    .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                    .build();
                build.start();
                try {
                    distributedBarrier[0] = new DistributedDoubleBarrier(build, path, 5);
                    Thread.sleep(Math.round(Math.random()*3000));
                    System.out.println(finalI+"进入"+System.currentTimeMillis());
                    distributedBarrier[0].enter();
                    System.out.println(finalI+"启动"+System.currentTimeMillis());
                    Thread.sleep(Math.round(Math.random()*3000));
                    distributedBarrier[0].leave();
                    System.out.println(finalI+"离开"+System.currentTimeMillis());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }).start();
        }
    }

}




