package Algorithms.Stripe;

import Models.*;

public class StripeMatrixMultiplicator implements MatrixMultiplicator {
    private final int countOfThreads;
    private final Result result = null;

    public StripeMatrixMultiplicator(int countOfThreads) {
        this.countOfThreads = countOfThreads;
    }

    @Override
    public Result multiply(Matrix m1, Matrix m2) {
        if (m1.getDimensionY() != m2.getDimensionX()) {
            throw new IllegalArgumentException("Not right size for multiplication");
        }

        Result result = new Result(m1.getDimensionX(), m2.getDimensionY());
        StripeThread[] threads = this.createThreads(m1, m2, result);

        for (StripeThread thread : threads) {
            thread.start();
        }

        for (StripeThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    private StripeThread[] createThreads(Matrix m1, Matrix m2, Result result) {

        final int countOfGroups = Math.min(m1.getDimensionX(), this.countOfThreads);
        final int rowGroupSize = m1.getDimensionX() / countOfGroups;
        final int colGroupSize = m2.getDimensionY() / countOfGroups;

        final Matrix m2Transposed = m2.transposed();
        final StripeThread[] threads = new StripeThread[countOfGroups];
        StripeSyncer syncer = new StripeSyncer(threads, countOfGroups);

        for(int i = 0; i < countOfGroups; i++) {
            int currentRowSize = rowGroupSize;
            int currentColumnSize = colGroupSize;
            if (i == countOfGroups - 1) {
                currentRowSize = Math.max(rowGroupSize, m1.getDimensionX() - i * rowGroupSize);
                currentColumnSize = Math.max(colGroupSize, m2Transposed.getDimensionX() - i * colGroupSize);
            }

            Matrix rowGroup = m1.getRows(i * rowGroupSize, currentRowSize);
            Matrix columnGroup = m2Transposed.getRows(i * colGroupSize, currentColumnSize);
            StripeThread stripeThread = new StripeThread(rowGroup, i * rowGroupSize, columnGroup, i * colGroupSize, result, countOfGroups, syncer);
            threads[i] = stripeThread;
        }

        return threads;
    }
}
