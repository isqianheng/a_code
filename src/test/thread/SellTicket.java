package test.thread;
/**多线程模拟卖票
 *   假设有火车票100张，创建10个线程模拟10个售票点，每个售票点100毫秒卖一张票。 打印出售票过程，注意使用synchronized确保同一张票只能卖出一次。输出格式如下：
     第4售票点卖出第100张票
     第2售票点卖出第101张票 …… 
 */
public class SellTicket {
	static int tickets = 100; 
	 
    public static void main(String[] args) {
         
        for(int i=1;i<=10;i++){
            Thread t = new Thread(new Runnable() {
                public void run(){
                    while(true){
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(tickets<=0){
                            System.out.println(Thread.currentThread().getName()+"票已卖完！");
                            break;
                        }
                        synchronized (SellTicket.class){
                            System.out.println(Thread.currentThread().getName()+"卖出第"+(tickets--)+"张票");
                        }
                    }
                }
            },"第"+i+"售票点");
            t.start();
        }
    }
}
