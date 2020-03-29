package com.mzj.zookeeper.lock.demo.InterProcessReadWriteLock;

import com.mzj.zookeeper.lock.demo.TestBase;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * 读锁和读锁不互斥，只要有写锁就互斥。
 *
 * 本示例测试两个客户端可以同时获取读锁
 */
public class InterProcessReadWriteLockTest extends TestBase {

    @Test
    public void sharedReentrantReadWriteLock() throws Exception {
        // 创建可重入读写锁
        final InterProcessReadWriteLock lock1 = new InterProcessReadWriteLock(client, lockPath);
        // lock2 用于模拟其他客户端
        final InterProcessReadWriteLock lock2 = new InterProcessReadWriteLock(client2, lockPath);

        // 获取读锁(使用 InterProcessMutex 实现, 所以是可以重入的)
        final InterProcessLock readLock = lock1.readLock();
        final InterProcessLock readLockw = lock2.readLock();

        final CountDownLatch countDownLatch = new CountDownLatch(2);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取锁对象
                try {
                    System.out.println("client...1...请求读锁===============");
                    readLock.acquire();
                    System.out.println("client...1...获取读锁===============");
                    // 测试锁重入
                    readLock.acquire();
                    System.out.println("client...1...再次获取读锁===============");
                    Thread.sleep(5 * 1000);
                    readLock.release();
                    System.out.println("client...1...释放读锁===============");
                    readLock.release();
                    System.out.println("client...1...再次释放读锁===============");

                    countDownLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取锁对象
                try {
                    Thread.sleep(500);
                    System.out.println("client...2...请求读锁===============");
                    readLockw.acquire();
                    System.out.println("client...2...获取读锁===============");
                    // 测试锁重入
                    readLockw.acquire();
                    System.out.println("client...2...再次获取读锁==============");
                    Thread.sleep(5 * 1000);
                    readLockw.release();
                    System.out.println("client...2...释放读锁===============");
                    readLockw.release();
                    System.out.println("client...2...再次释放读锁===============");

                    countDownLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        countDownLatch.await();
    }
}
