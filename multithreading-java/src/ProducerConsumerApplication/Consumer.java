package ProducerConsumerApplication;

import java.util.Random;

public class Consumer implements Runnable {
    private final Drop drop;
    private final int arraySize;

    public Consumer(Drop drop, int arraySize) {
        this.drop = drop;
        this.arraySize = arraySize;
    }

    public void run() {
        Random random = new Random();
        for (int i = 0; i < arraySize; i++) {
            drop.take();
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}