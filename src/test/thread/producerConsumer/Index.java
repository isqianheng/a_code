package test.thread.producerConsumer;

public class Index {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Stack stack = new Stack(20);
		Producer producer = new Producer(stack);
		Consumer consumer = new Consumer(stack);
		new Thread(producer).start();
		new Thread(consumer).start();
	}

}
