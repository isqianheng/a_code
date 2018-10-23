package test.thread.producerConsumer;

public class Consumer implements Runnable {
	Stack s = null;
	public Consumer(Stack stack) {
		this.s = stack;
	}
	public void run() {
		// TODO Auto-generated method stub
		for (int i=0;i<110;i++){
			Apple apple = this.s.get();
			System.out.println("消费了"+apple);
			try {
				Thread.sleep((long)Math.random()*4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
