package Algorithms.Stripe;

import Models.*;

public class StripeThread extends Thread {
    private final Matrix rowGroup;
    private final int aOffset;
    private int bOffset;
    private Matrix columnGroup;
    private final Result result;
    int iterations;
    private final StripeSyncer syncer;

    public StripeThread(Matrix rowGroup, int aOffset, Matrix columnGroup, int bOffset, Result result, int iterations, StripeSyncer syncer) {
        this.rowGroup = rowGroup;
        this.aOffset = aOffset;
        this.bOffset = bOffset;
        this.columnGroup = columnGroup;
        this.result = result;
        this.iterations = iterations;
        this.syncer = syncer;
    }

    @Override
    public void run() {
        for (int i = 0; i < iterations; i++){
            multiplyMatrices();
            syncer.update(i);
        }
    }

    private void multiplyMatrices() {
        for (int i = 0; i < rowGroup.getDimensionX(); i++) {
            for (int j = 0; j < columnGroup.getDimensionX(); j++) {
                for (int k = 0; k < columnGroup.getDimensionY(); k++) {
                    this.result.data[i + aOffset][j + bOffset] += rowGroup.get(i, k) * columnGroup.get(j, k);
                }
            }
        }
    }

    public Matrix getColumnGroup() {
        return columnGroup;
    }

    public void setColumnGroup(Matrix columnGroup) {
        this.columnGroup = columnGroup;
    }

    public void setbOffset(int offset) {
        bOffset = offset;
    }

    public int getbOffset() {
        return bOffset;
    }
}
