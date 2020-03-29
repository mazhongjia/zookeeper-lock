package com.mzj.zookeeper.lock.demo.otherdemo;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class SemaphoreMutexDemo {
    private static CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", new ExponentialBackoffRetry(3000, 3));
    private static String path = "/locks/semaphore/0001";
    static {
        client.start();
    }

    public static void main(String[] args) throws Exception {
        startThread0();
        Thread.sleep(10);
        startThread1();
        Thread.sleep(50000);
        client.close();
    }

    private static void startThread0() {
        new Thread(() -> {
            InterProcessSemaphoreMutex lock = new InterProcessSemaphoreMutex(client, path);
            try {
                System.out.println("thread0 acquiring");
                lock.acquire();
                System.out.println("thread0 acquired");
                System.out.println("thread0 sleep for 3s");
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    lock.release();
                    System.out.println("thread0 release");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static void startThread1() {
        new Thread(() -> {
            InterProcessSemaphoreMutex lock = new InterProcessSemaphoreMutex(client, path);
            try {
                System.out.println("thread1 acquiring");
                lock.acquire();
                System.out.println("thread1 acquired");
                System.out.println("thread1 sleep for 3s");
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    lock.release();
                    System.out.println("thread1 release");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
