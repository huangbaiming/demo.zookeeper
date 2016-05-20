package demo.zookeeper;

import java.util.concurrent.TimeUnit;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryForever;

public class AccountLock {
	static CuratorFramework client;

	static {
		/* 初始化工厂 */
		String connectString = "10.211.55.4:2181";
		int sessionTimeoutMs = 1000 * 5;// session超时(毫秒)
		int connectionTimeoutMs = 1000 * 2;// 连接超时(毫秒)
		int retryIntervalMs = 1000 * 5;// 重连间隔
		RetryPolicy retryPolicy = new RetryForever(retryIntervalMs);
		client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
		client.start();
	}

	InterProcessMutex lock;
	boolean wasAcquired;

	public AccountLock(String id) {
		lock = new InterProcessMutex(client, "/ACCOUNT/" + id);
	}

	/**
	 * 尝试加锁并等待
	 * 
	 * @param timeOut
	 *            超时时间（秒）, -1 不限时
	 * @return true为获取锁成功，false为获取锁失败
	 * 
	 * @throws Exception
	 */
	public boolean acquire(int timeOut) throws Exception {
		wasAcquired = lock.acquire(timeOut, TimeUnit.SECONDS);
		return wasAcquired;
	}

	/**
	 * 释放锁
	 * 
	 * @throws Exception
	 */
	public void release() throws Exception {
		if (wasAcquired) {
			lock.release();
		}
	}

}
