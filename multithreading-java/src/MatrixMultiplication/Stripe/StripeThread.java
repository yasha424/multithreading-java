package MatrixMultiplication.Stripe;

import MatrixMultiplication.Models.*;

public class StripeThread extends Thread {
    private final Matrix rowGroup;
    private final int aOffset;
    private final Matrix columnGroup;
    private final Matrix result;

    public StripeThread(Matrix rowGroup, int aOffset, Matrix columnGroup, Matrix result) {
        this.rowGroup = rowGroup;
        this.aOffset = aOffset;
        this.columnGroup = columnGroup;
        this.result = result;
    }

    @Override
    public void run() {
        for(int i = 0; i < rowGroup.getDimensionX(); i++) {
            for(int j = 0; j < columnGroup.getDimensionX(); j++) {
                for (int k = 0; k < columnGroup.getDimensionY(); k++)
                    this.result.data[i + aOffset][j] += rowGroup.get(i, k) * columnGroup.get(j, k);
            }
        }
    }
}