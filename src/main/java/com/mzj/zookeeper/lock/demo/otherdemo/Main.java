package com.mzj.zookeeper.lock.demo.otherdemo;

/**
 * @Auther: mazhongjia
 * @Date: 2020/1/10 15:44
 * @Version: 1.0
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        DistributedLock distributedLock = new DistributedLock("mylock");
        System.out.println("开始获取锁。。。。");
        boolean result = distributedLock.acquireLock();
        System.out.println((result ? "成功" : "失败" )+ "获取锁。。。。");
        Thread.sleep(Integer.MAX_VALUE);
    }
}
