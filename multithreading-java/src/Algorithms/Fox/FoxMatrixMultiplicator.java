package Algorithms.Fox;

import Models.*;

public class FoxMatrixMultiplicator implements MatrixMultiplicator {

    final int blocksNumSqrt;

    public FoxMatrixMultiplicator(int blocksNumSqrt) {
        this.blocksNumSqrt = blocksNumSqrt;
    }

    public Result multiply(Matrix m1, Matrix m2) {
        if (m1.getDimensionY() != m2.getDimensionX()) {
            throw new IllegalArgumentException("Illegal size of matrices for multiplication");
        } else if (m1.getDimensionX() % blocksNumSqrt != 0 || m1.getDimensionY() % blocksNumSqrt != 0 ||
                   m2.getDimensionX() % blocksNumSqrt != 0 || m2.getDimensionY() % blocksNumSqrt != 0 ||
                   m1.getDimensionX() != m1.getDimensionY() || m2.getDimensionX() != m2.getDimensionY()) {
            throw new IllegalArgumentException("Matrix must be squared and be a multiple of the square root of the number of threads.");
        }

        int blockSize = m1.getDimensionX() / blocksNumSqrt;

        FoxThread[][] threads = new FoxThread[blocksNumSqrt][blocksNumSqrt];
        FoxSyncer syncer = new FoxSyncer(threads, m1, m2);

        for (int i = 0; i < blocksNumSqrt; i++) {
            Matrix m1Block = m1.getSquareBlock(i * blockSize, i * blockSize, blockSize);
            for (int j = 0; j < blocksNumSqrt; j++) {
                Matrix m2Block = m2.getSquareBlock(i * blockSize, j * blockSize, blockSize);
                threads[i][j] = new FoxThread(m1Block, m2Block, blocksNumSqrt, syncer);
            }
        }

        for (FoxThread[] threadsRow : threads) {
            for (FoxThread thread : threadsRow) {
                thread.start();
            }
        }

        for (FoxThread[] threadsRow : threads) {
            for (FoxThread thread : threadsRow) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        Matrix[][] blocks = new Matrix[blocksNumSqrt][blocksNumSqrt];

        for (int i = 0; i < blocksNumSqrt; i++) {
            for (int j = 0; j < blocksNumSqrt; j++) {
                blocks[i][j] = threads[i][j].getResult();
            }
        }

        Result result = new Result(m1.getDimensionX(), m2.getDimensionY());
        result.joinBlocks(blocks);
        return result;
    }
}
