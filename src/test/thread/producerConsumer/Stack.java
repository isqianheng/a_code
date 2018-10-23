package test.thread.producerConsumer;

public class Stack {
	int index=0;
	int maxCount = 8;
	Apple[] appArray = new Apple[maxCount];
	Stack(){
		
	}
	Stack(int maxCount) {
		this.maxCount = maxCount;
	}
	public synchronized void put(Apple apple) {
		while(index == maxCount){
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.notifyAll();
		appArray[index] = apple;
		index++;
		
	}
	public synchronized Apple get() {
		while(index == 0){
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.notifyAll();
		index--;
		return 	appArray[index];
	}
}
