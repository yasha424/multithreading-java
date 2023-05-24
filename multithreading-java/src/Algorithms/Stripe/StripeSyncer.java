package Algorithms.Stripe;

public class StripeSyncer {
    int iterations;
    int pausedThreads = 0;
    int finishedIterations = 0;
    StripeThread[] threads;

    StripeSyncer(StripeThread[] threads, int iterations) {
        this.iterations = iterations;
        this.threads = threads;
    }

    public synchronized void update(int iteration) {
        pausedThreads++;

        while(pausedThreads < threads.length) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (iteration < finishedIterations) {
                break;
            }
        }

        if (pausedThreads == threads.length) {
            updateThreads();
            pausedThreads = 0;
            finishedIterations++;
            notifyAll();
        }
    }

    void updateThreads() {
        var firstColGroup = threads[0].getColumnGroup();
        var firstbOffset = threads[0].getbOffset();
        for (int i = 0; i < threads.length - 1; i++) {
            threads[i].setColumnGroup(threads[(i + 1) % threads.length].getColumnGroup());
            threads[i].setbOffset(threads[(i + 1) % threads.length].getbOffset());
        }
        threads[threads.length - 1].setbOffset(firstbOffset);
        threads[threads.length - 1].setColumnGroup(firstColGroup);
    }
}
