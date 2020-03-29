package com.mzj.zookeeper.lock.demo.InterProcessMutex;

import com.mzj.zookeeper.lock.demo.TestBase;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class InterProcessMutexTest2 extends TestBase {

    /**
     * 测试公平性
     *
     * 执行流程：
     *
     * 1、client1先占有锁
     * 4、client1等待8s后释放锁
     *
     * 2、client2在client1先占有锁2s后请求锁（死等）
     *
     * 7、client2在client3释放锁后成功获取锁
     * 8、client2等待5s后释放锁、再释放锁
     *
     * 3、client3再client1先占有锁3s后请求锁（死等），client
     * 5、client3在client1释放锁后成功获取锁（因为client3先排队）
     * 6 、client3等待5s后释放锁
     * @throws Exception
     */
    @Test
    public void sharedReentrantLock() throws Exception {
        // 创建锁
        final InterProcessLock lock = new InterProcessMutex(client, lockPath);
        // lock2 用于模拟其他客户端
        final InterProcessLock lock2 = new InterProcessMutex(client2, lockPath);
        // lock3 用于模拟其他客户端
        final InterProcessLock lock3 = new InterProcessMutex(client3, lockPath);

        final CountDownLatch countDownLatch = new CountDownLatch(3);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取锁对象
                try {
                    System.out.println("client...1...请求锁===============");
                    lock.acquire();
                    System.out.println("client...1...获取锁===============");
                    Thread.sleep(8 * 1000);
                    lock.release();
                    System.out.println("client...1...释放锁===============");

                    countDownLatch.countDown();
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
                    System.out.println("client...2...请求锁===============");
                    lock2.acquire();
                    System.out.println("client...2...获取锁===============");
                    Thread.sleep(5 * 1000);
                    lock2.release();
                    System.out.println("client...2...释放锁===============");

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

                    System.out.println("client...3...请求锁===============");
                    lock3.acquire();
                    System.out.println("client...3...获取锁===============");
                    Thread.sleep(5 * 1000);
                    lock3.release();
                    System.out.println("client...3...释放锁===============");

                    countDownLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        countDownLatch.await();
    }
}
