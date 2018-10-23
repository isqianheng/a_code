package test.thread;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class ThreadPool {
	public static void main(String[] args) {
		ExecutorService service = Executors.newFixedThreadPool(3);//最多只允许同时三个线程
		for (int i=0;i<1000;i++){
			service.execute(new Runnable() {
				public void run() {
					System.out.println(Thread.currentThread().getName());
				}
			});
		}
	}
}
