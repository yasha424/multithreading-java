package Counter;

import java.util.concurrent.locks.ReentrantLock;

public class Counter {
    private int count = 0;

    private ReentrantLock locker = new ReentrantLock();

    public void syncBlockIncrement() {
        synchronized (this) {
            count++;
        }
    }

    public void syncBlockDecrement() {
        synchronized (this) {
            count--;
        }
    }

    public void lockerIncrement() {
        try {
            locker.lock();
            count++;
        } finally {
            locker.unlock();
        }
    }

    public void lockerDecrement() {
        try {
            locker.lock();
            count--;
        } finally {
            locker.unlock();
        }
    }

    public synchronized void syncIncrement() {
        count++;
    }

    public synchronized void syncDecrement() {
        count--;
    }

    public void increment() {
        count++;
    }

    public void decrement() {
        count--;
    }

    public int getCount() {
        return count;
    }
}
