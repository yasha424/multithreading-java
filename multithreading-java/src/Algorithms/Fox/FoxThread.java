package Algorithms.Fox;

import Models.*;

public class FoxThread extends Thread {
    private Matrix m1;
    private Matrix m2;
    private final int iterations;
    private final FoxSyncer syncer;
    private final Matrix result;


    FoxThread(Matrix m1, Matrix m2, int iterations, FoxSyncer syncer) {
        this.m1 = m1;
        this.m2 = m2;
        this.iterations = iterations;
        this.syncer = syncer;

        result = new Matrix(m1.getDimensionX(), m2.getDimensionY());
    }

    @Override
    public void run() {
        for (int i = 0; i < iterations; i++) {
            multiplyMatrices();
            syncer.update(i);
        }
    }

    private void multiplyMatrices() {
        for (int i = 0; i < m1.getDimensionX(); i++) {
            for (int j = 0; j < m2.getDimensionY(); j++) {
                for (int k = 0; k < m2.getDimensionX(); k++) {
                    result.data[i][j] += m1.data[i][k] * m2.data[k][j];
                }
            }
        }
    }

    public void setM1Block(Matrix block) {
        this.m1 = block;
    }

    public void setM2Block(Matrix block) {
        this.m2 = block;
    }

    public Matrix getResult() {
        return result;
    }

}
