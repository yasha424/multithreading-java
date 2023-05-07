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
//        for (String message = drop.take(); !message.equals("DONE"); message = drop.take()) {
//            System.out.format("MESSAGE RECEIVED: %s%n", message);
//            try {
//                Thread.sleep(random.nextInt(5000));
//            } catch (InterruptedException e) {}
//        }
        for (int i = 0; i < arraySize; i++) {
//            System.out.println("Recieved: " + drop.take());
            drop.take();
            try {
                Thread.sleep(random.nextInt(5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}