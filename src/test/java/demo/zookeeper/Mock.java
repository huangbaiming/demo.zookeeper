package demo.zookeeper;

/**
 * 模拟业务执行
 * 
 * @author hbm
 *
 */
public class Mock {

	public static void doSomething() {
		try {
			System.out.println("开始工作");
			Thread.sleep(1000 * 20);
			System.out.println("工作完毕");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
