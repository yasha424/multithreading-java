package Algorithms.Stripe;

import Models.*;

public class StripeMatrixMultiplicator implements MatrixMultiplicator {
    private final int countOfThreads;
    private Result result = null;

    public StripeMatrixMultiplicator(int countOfThreads) {
        this.countOfThreads = countOfThreads;
    }

    @Override
    public Result multiply(Matrix m1, Matrix m2) {
        if (m1.getDimensionY() != m2.getDimensionX()) {
            throw new IllegalArgumentException("Not right size for multiplication");
        }

        this.result = new Result(m1.getDimensionX(), m2.getDimensionY());
        StripeThread[] threads = this.createThreads(m1, m2);

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

        return this.result;
    }

    private StripeThread[] createThreads(Matrix m1, Matrix m2) {

        final int countOfGroups = Math.min(m1.getDimensionX(), this.countOfThreads);
        final int rowGroupSize = m1.getDimensionX() / countOfGroups;

        final Matrix m2Transposed = m2.transposed();
        final StripeThread[] threads = new StripeThread[countOfGroups];

        for(int i = 0; i < countOfGroups; i++) {
            int currentRowSize = rowGroupSize;
            if (i == countOfGroups - 1) {
                currentRowSize = Math.max(rowGroupSize, m1.getDimensionX() - i * rowGroupSize);
            }

            Matrix rowGroup = m1.getRows(i * rowGroupSize, currentRowSize);

            StripeThread stripeThread = new StripeThread(rowGroup, i * rowGroupSize, m2Transposed, this.result);

            threads[i] = stripeThread;

        }

        return threads;
    }
}
