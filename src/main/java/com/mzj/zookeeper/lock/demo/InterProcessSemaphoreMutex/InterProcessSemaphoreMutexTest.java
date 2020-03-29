package com.mzj.zookeeper.lock.demo.InterProcessSemaphoreMutex;

import com.mzj.zookeeper.lock.demo.TestBase;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.junit.Test;

/**
 * 测试InterProcessSemaphoreMutex的简单使用（与InterProcessMutex用法基本一致）
 */
public class InterProcessSemaphoreMutexTest extends TestBase {

    @Test
    public void sharedLock() throws Exception {
        // 创建锁
        final InterProcessLock lock = new InterProcessSemaphoreMutex(client, lockPath);
        // lock2 用于模拟其他客户端
        final InterProcessLock lock2 = new InterProcessSemaphoreMutex(client2, lockPath);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取锁对象
                try {
                    System.out.println("client...1...请求锁===============");
                    lock.acquire();
                    System.out.println("client...1...获取锁===============");
                    Thread.sleep(5 * 1000);
                    lock.release();
                    System.out.println("client...1...释放锁===============");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Thread.sleep(2 * 1000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取锁对象
                try {
                    System.out.println("client...2...请求锁===============");
                    lock2.acquire();
                    System.out.println("client...2...获取锁===============");
                    Thread.sleep(5 * 1000);
                    lock2.release();
                    System.out.println("client...2...释放锁===============");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Thread.sleep(20 * 1000);
    }
}
