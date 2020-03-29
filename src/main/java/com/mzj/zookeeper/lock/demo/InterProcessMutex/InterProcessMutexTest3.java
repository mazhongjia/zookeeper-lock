package com.mzj.zookeeper.lock.demo.InterProcessMutex;

import com.mzj.zookeeper.lock.demo.TestBase;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class InterProcessMutexTest3 extends TestBase {

    /**
     *
     *  使用同一个client创建锁，在不同线程中使用（模拟，一个JVM只创建一个client，不论这个JVM中多少个线程使用锁，请求是否相同的锁）
     *
     *  本示例测试同一个client获取不同的lockPath
     *
     * @throws Exception
     */
    @Test
    public void sharedReentrantLock() throws Exception {
        // 创建锁
        final InterProcessLock lock = new InterProcessMutex(client, lockPath);
        final InterProcessLock lock2 = new InterProcessMutex(client, lockPath2);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取锁对象
                try {
                    System.out.println("lock...1...请求锁===============");
                    lock.acquire();
                    System.out.println("lock...1...获取锁===============");
                    Thread.sleep(8 * 1000);
                    lock.release();
                    System.out.println("lock...1...释放锁===============");

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
                    Thread.sleep(1 * 1000);
                    System.out.println("lock...2...请求锁===============");
                    lock2.acquire();
                    System.out.println("lock...2...获取锁===============");
                    Thread.sleep(5 * 1000);
                    lock2.release();
                    System.out.println("lock...2...释放锁===============");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Thread.sleep(20 * 1000);

    }
}
