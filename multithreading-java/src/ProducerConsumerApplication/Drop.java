package ProducerConsumerApplication;

import java.util.Arrays;

public class Drop {
    private Double[] buffer;
    private int elementsInBuffer = 0;

    public Drop(int bufferSize) {
        this.buffer = new Double[bufferSize];
    }

    public synchronized Double take() {
        while (elementsInBuffer < 1) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        elementsInBuffer--;
        var message = buffer[elementsInBuffer];
        notifyAll();
        return message;
    }

    public synchronized void put(Double message) {
        while (elementsInBuffer >= buffer.length) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        buffer[elementsInBuffer] = message;
        elementsInBuffer++;
        notifyAll();
    }
}