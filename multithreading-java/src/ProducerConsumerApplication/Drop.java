package ProducerConsumerApplication;

public class Drop {
    private Double message;
    private boolean empty = true;

    public synchronized Double take() {
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        empty = true;
        System.out.println("Received: " + message);
        notifyAll();
        return message;
    }

    public synchronized void put(Double message) {
        while (!empty) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        empty = false;
        this.message = message;
        System.out.println("Sent: " + message);
        notifyAll();
    }
}