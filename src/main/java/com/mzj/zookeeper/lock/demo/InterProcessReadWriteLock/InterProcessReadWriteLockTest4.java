package com.mzj.zookeeper.lock.demo.InterProcessReadWriteLock;

import com.mzj.zookeeper.lock.demo.TestBase;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * 读锁和读锁不互斥，只要有写锁就互斥。
 *
 * 本示例测试两个客户端其中一个如果获取了写锁，另一个不可以获取读锁，只有写锁释放后才能获取
 */
public class InterProcessReadWriteLockTest4 extends TestBase {

    @Test
    public void sharedReentrantReadWriteLock() throws Exception {
        // 创建可重入读写锁
        final InterProcessReadWriteLock lock1 = new InterProcessReadWriteLock(client, lockPath);
        // lock2 用于模拟其他客户端
        final InterProcessReadWriteLock lock2 = new InterProcessReadWriteLock(client2, lockPath);

        // 获取读锁(使用 InterProcessMutex 实现, 所以是可以重入的)
        final InterProcessLock readLock = lock1.readLock();
        final InterProcessLock writeLock = lock2.writeLock();

        final CountDownLatch countDownLatch = new CountDownLatch(2);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取锁对象
                try {
                    Thread.sleep(2000);
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
                    System.out.println("client...2...请求写锁===============");
                    writeLock.acquire();
                    System.out.println("client...2...获取写锁===============");
                    // 测试锁重入
                    writeLock.acquire();
                    System.out.println("client...2...再次获取写锁==============");
                    Thread.sleep(5 * 1000);
                    writeLock.release();
                    System.out.println("client...2...释放写锁===============");
                    writeLock.release();
                    System.out.println("client...2...再次释放写锁===============");

                    countDownLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        countDownLatch.await();
    }
}
