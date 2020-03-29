package com.mzj.zookeeper.lock.demo;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.junit.After;
import org.junit.Before;

public class TestBase {

    // ZooKeeper 锁节点路径, 分布式锁的相关操作都是在这个节点上进行
    protected final String lockPath = "/distributed-lock";
    protected final String lockPath2 = "/distributed-lock2";

    // ZooKeeper 服务地址, 单机格式为:(127.0.0.1:2181),
    // 集群格式为:(127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183)
    private String connectString;
    // Curator 客户端重试策略
    private RetryPolicy retry;
    // Curator 客户端对象
    protected CuratorFramework client;
    // client2 用户模拟其他客户端
    protected CuratorFramework client2;
    // client3 用户模拟其他客户端
    protected CuratorFramework client3;

    // 初始化资源
    @Before
    public void init() throws Exception {
        // 设置 ZooKeeper 服务地址为本机的 2181 端口
        connectString = "127.0.0.1:2181";
        // 重试策略
        // 初始休眠时间为 1000ms, 最大重试次数为 3
        retry = new ExponentialBackoffRetry(1000, 3);
        // 创建一个客户端, 60000(ms)为 session 超时时间, 15000(ms)为链接超时时间
        client = CuratorFrameworkFactory.newClient(connectString, 60000, 15000, retry);
        client2 = CuratorFrameworkFactory.newClient(connectString, 60000, 15000, retry);
        client3 = CuratorFrameworkFactory.newClient(connectString, 60000, 15000, retry);
        // 创建会话
        client.start();
        client2.start();
        client3.start();
    }

    // 释放资源
    @After
    public void close() {
        CloseableUtils.closeQuietly(client);
        CloseableUtils.closeQuietly(client2);
        CloseableUtils.closeQuietly(client3);
    }
}
