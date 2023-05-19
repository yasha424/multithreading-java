package ProducerConsumerApplication;

public class ProducerConsumerExample {
    public static void main(String[] args) {
        Drop drop = new Drop(100);
        (new Thread(new Producer(drop, 100))).start();
        (new Thread(new Consumer(drop))).start();
    }
}