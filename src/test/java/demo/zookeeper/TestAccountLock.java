package demo.zookeeper;

/**
 * 账户锁示例
 * 
 * @author hbm
 *
 */
public class TestAccountLock {

	public static void main(String[] args) {
		AccountLock lock = new AccountLock("abc");

		boolean wasAcquired;// 是否获取了锁
		try {
			// 获取锁，5秒超时
			wasAcquired = lock.acquire(5);
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
