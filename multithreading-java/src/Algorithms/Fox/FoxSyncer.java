package Algorithms.Fox;

import Models.Matrix;

public class FoxSyncer {
    private final FoxThread[][] threads;
    private int pausedThreads = 0;
    private int finishedIterations = 0;

    private final Matrix m1;
    private final Matrix m2;

    FoxSyncer(FoxThread[][] threads, Matrix m1, Matrix m2) {
        this.threads = threads;
        this.m1 = m1;
        this.m2 = m2;
    }

    public synchronized void update(int iteration) {
        pausedThreads++;
        while(pausedThreads < threads.length * threads[0].length) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (iteration < finishedIterations) {
                break;
            }
        }
        if (pausedThreads == threads.length * threads[0].length) {
            updateThreads(iteration);
            pausedThreads = 0;
            finishedIterations++;
            notifyAll();
        }
    }

    private void updateThreads(int iteration) {
        int blockSize = m1.getDimensionX() / threads.length;
        for (int i = 0; i < threads.length; i++) {
            int k = (i * blockSize + (iteration + 1) * blockSize) % (m1.getDimensionY());
            var m1Block = m1.getSquareBlock(i * blockSize, k, blockSize);

            for (int j = 0; j < threads[0].length; j++) {
                threads[i][j].setM1Block(m1Block);
                threads[i][j].setM2Block(m2.getSquareBlock(k, j * blockSize, blockSize));
            }
        }
    }
}
