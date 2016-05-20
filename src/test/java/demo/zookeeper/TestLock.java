package demo.zookeeper;

import java.util.concurrent.TimeUnit;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryForever;

/**
 * 分布式锁示例
 * 
 * @author hbm
 *
 */
public class TestLock {
	static CuratorFramework client;

	static {
		/** 常用的4种重连策略： **/

		int retryIntervalMs = 1000 * 5;// 重试间隔时间
		/* 持续重试 */
		RetryPolicy retryPolicy1 = new RetryForever(retryIntervalMs);

		// int sleepMsBetweenRetries = 1000 * 5; // 睡眠间隔时间
		// /* 重试n次 */
		// RetryPolicy retryPolicy2 = new RetryNTimes(100,
		// sleepMsBetweenRetries);
		//
		// int maxElapsedTimeMs = 1000 * 60 * 30;// 总时长
		// /* 重连多次超过该时长则停止重连 */
		// RetryPolicy retryPolicy3 = new RetryUntilElapsed(maxElapsedTimeMs,
		// sleepMsBetweenRetries);
		//
		// int baseSleepTimeMs = 1000 * 5;
		// int maxRetries = 100;
		// int maxSleepMs = 1000 * 60;
		// /* 按间隔递增睡眠时间，直到超过最大重连次数 */
		// RetryPolicy retryPolicy4 = new
		// ExponentialBackoffRetry(baseSleepTimeMs, maxRetries, maxSleepMs);

		String connectString = "10.211.55.4:2181";
		int sessionTimeoutMs = 1000 * 5;// session超时(毫秒)
		int connectionTimeoutMs = 1000 * 2;// 连接超时(毫秒)
		client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy1);
		client.start();
	}

	public static void main(String[] args) {
		// 初始化锁
		InterProcessMutex lock = new InterProcessMutex(client, "/ACCOUNT/123456789");

		boolean wasAcquired;// 是否获取了锁
		try {
			// 获取锁，5秒超时
			wasAcquired = lock.acquire(5, TimeUnit.SECONDS);
		} catch (Exception e) {
			wasAcquired = false;
			System.out.println("获取锁异常");
			e.printStackTrace();
		}

		if (wasAcquired) {
			Mock.doSomething(); // 执行业务
			try {
				lock.release();// 释放锁
			} catch (Exception e) {
				System.out.println("释放锁异常");
				e.printStackTrace();
			}
		} else {
			System.out.println("获取锁失败");
		}

	}

}
