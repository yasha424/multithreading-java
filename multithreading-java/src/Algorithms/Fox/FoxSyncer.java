package Algorithms.Fox;

import Models.Matrix;

public class FoxSyncer {
    private final FoxThread[][] threads;
    private int pausedThreads = 0;
    private int finishedIterations = 0;

    private final Matrix m1;

    FoxSyncer(FoxThread[][] threads, Matrix m1) {
        this.threads = threads;
        this.m1 = m1;
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
        Matrix[] firstM2Blocks = new Matrix[threads.length];

        for (int i = 0; i < threads[0].length; i++) {
            firstM2Blocks[i] = threads[0][i].getM2Block();
        }

        for (int i = 0; i < threads.length - 1; i++) {
            int k = (i * blockSize + (iteration + 1) * blockSize) % (m1.getDimensionY());
            var m1Block = m1.getSquareBlock(i * blockSize, k, blockSize);

            for (int j = 0; j < threads[i].length; j++) {
                threads[i][j].setM1Block(m1Block);
                threads[i][j].setM2Block(threads[i + 1][j].getM2Block());
            }
        }

        int k = ((threads.length + iteration) * blockSize) % (m1.getDimensionY());
        var m1Block = m1.getSquareBlock((threads.length - 1) * blockSize, k, blockSize);
        for (int i = 0; i < firstM2Blocks.length; i++) {
            threads[threads.length - 1][i].setM1Block(m1Block);
            threads[threads.length - 1][i].setM2Block(firstM2Blocks[i]);
        }
    }
}
