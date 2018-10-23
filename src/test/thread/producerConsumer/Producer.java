package test.thread.producerConsumer;
public class Producer implements Runnable {
	Stack s = null;
	Producer(Stack stack){
		this.s = stack;	
	}
	public void run() {
		for(int i=0;i<100;i++){
			Apple apple = new Apple(i);
			this.s.put(apple);
			System.out.println("生产了"+apple);
			try {
				Thread.sleep((long)Math.random()*300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
