package com.mzj.zookeeper.lock.demo.InterProcessSemaphoreMutex;

import com.mzj.zookeeper.lock.demo.TestBase;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.junit.Test;

/**
 * 测试InterProcessSemaphoreMutex的不可重入性
 */
public class InterProcessSemaphoreMutexTest2 extends TestBase {

    @Test
    public void sharedLock() throws Exception {
        // 创建锁
        final InterProcessLock lock = new InterProcessSemaphoreMutex(client, lockPath);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取锁对象
                try {
                    System.out.println("client...1...请求锁===============");
                    lock.acquire();
                    System.out.println("client...1...获取锁===============");

                    System.out.println("client...1...再次请求锁===============");
                    lock.acquire();
                    System.out.println("client...1...再次获取锁===============");

                    Thread.sleep(5 * 1000);
                    lock.release();
                    System.out.println("client...1...释放锁===============");
                    lock.release();
                    System.out.println("client...1...再次释放锁===============");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


        Thread.sleep(20 * 1000);
    }
}
