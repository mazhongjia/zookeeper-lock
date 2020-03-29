package com.mzj.zookeeper.lock.demo.InterProcessMutex;

import com.mzj.zookeeper.lock.demo.TestBase;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class InterProcessMutexTest extends TestBase {

    /**
     * 测试重入性（重入性是同一个线程内、如果当前线程已经获取锁，则可以再次获取）
     *
     * 执行流程：
     *
     * 1、client1先占有锁
     * 2、client1再次获取锁
     * 4、client1等待5s后释放锁、再释放锁
     *
     * 3、client2再client1先占有锁2s后请求锁（死等）
     * 5、client2在client1释放锁后成功获取锁
     * 6、client2再次获取锁
     * 7、client2等待5s后释放锁、再释放锁
     * @throws Exception
     */
    @Test
    public void sharedReentrantLock() throws Exception {
        // 创建锁
        final InterProcessLock lock = new InterProcessMutex(client, lockPath);
        // lock2 用于模拟其他客户端
        final InterProcessLock lock2 = new InterProcessMutex(client2, lockPath);

        final CountDownLatch countDownLatch = new CountDownLatch(2);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取锁对象
                try {
                    System.out.println("client...1...请求锁===============");
                    lock.acquire();
                    System.out.println("client...1...获取锁===============");
                    // 测试锁重入
                    lock.acquire();
                    System.out.println("client...1...再次获取锁===============");
                    Thread.sleep(5 * 1000);
                    lock.release();
                    System.out.println("client...1...释放锁===============");
                    lock.release();
                    System.out.println("client...1...再次释放锁===============");

                    countDownLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Thread.sleep(2000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取锁对象
                try {
                    System.out.println("client...2...请求锁===============");
                    lock2.acquire();
                    System.out.println("client...2...获取锁===============");
                    // 测试锁重入
                    lock2.acquire();
                    System.out.println("client...2...再次获取锁===============");
                    Thread.sleep(5 * 1000);
                    lock2.release();
                    System.out.println("client...2...释放锁===============");
                    lock2.release();
                    System.out.println("client...2...再次释放锁===============");

                    countDownLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        countDownLatch.await();
    }
}
