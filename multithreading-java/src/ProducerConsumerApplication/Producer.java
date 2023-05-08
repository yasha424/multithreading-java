package ProducerConsumerApplication;

import java.util.Random;

public class Producer implements Runnable {
    private final Drop drop;
    private final int arraySize;

    public Producer(Drop drop, int arraySize) {
        this.drop = drop;
        this.arraySize = arraySize;
    }

    public void run() {
        Random random = new Random();

        for (int i = 0; i < arraySize; i++) {
            double number = random.nextDouble();

            drop.put(number);

            try {
                Thread.sleep(random.nextInt(5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}