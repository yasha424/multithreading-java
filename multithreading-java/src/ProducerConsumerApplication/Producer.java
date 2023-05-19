package ProducerConsumerApplication;

import java.util.Random;

public class Producer implements Runnable {
    private final Drop drop;
    private final int messagesToSend;

    public Producer(Drop drop, int messagesToSend) {
        this.drop = drop;
        this.messagesToSend = messagesToSend;
    }

    public void run() {
        Random random = new Random();

        for (int i = 0; i < messagesToSend; i++) {
            double number = random.nextDouble();

            drop.put(number);

            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        drop.put((double) -1);
    }
}