package ProducerConsumerApplication;

public class Drop {
    // Message sent from producer
    // to consumer.
    private Double message;
    // True if consumer should wait
    // for producer to send message,
    // false if producer should wait for
    // consumer to retrieve message.
    private boolean empty = true;

    public synchronized Double take() {
        // Wait until message is
        // available.
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        // Toggle status.
        empty = true;
        // Notify producer that
        // status has changed.
        notifyAll();
        System.out.println("Took: " + message);
        return message;
    }

    public synchronized void put(Double message) {
        // Wait until message has
        // been retrieved.
        while (!empty) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        // Toggle status.
        empty = false;
        // Store message.
        this.message = message;
        System.out.println("Putted: " + message);
        // Notify consumer that status
        // has changed.
        notifyAll();
    }
}