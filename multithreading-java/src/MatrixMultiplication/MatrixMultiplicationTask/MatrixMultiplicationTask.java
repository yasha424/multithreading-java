package MatrixMultiplication.MatrixMultiplicationTask;

import MatrixMultiplication.Models.Matrix;
import java.util.concurrent.RecursiveTask;

public class MatrixMultiplicationTask extends RecursiveTask<Matrix> {
    Matrix m1;
    Matrix m2;

    int threshold;

    public MatrixMultiplicationTask(Matrix m1, Matrix m2) {
        this.m1 = m1;
        this.m2 = m2;
        this.threshold = 8;
    }


    public MatrixMultiplicationTask(Matrix m1, Matrix m2, int threshold) {
        this.m1 = m1;
        this.m2 = m2;
        this.threshold = threshold;
    }

    @Override
    protected Matrix compute() {
        if (m1.getDimensionY() != m2.getDimensionX()) {
            throw new IllegalArgumentException("Illegal size for multiplication");
        }

        if (m1.getDimensionX() > threshold) {
            var middleRow = (int) Math.ceil((double)m1.getDimensionX() / 2.0);
            var subTask1 = new MatrixMultiplicationTask(m1.getRows(0, middleRow), m2);
            var subTask2 = new MatrixMultiplicationTask(m1.getRows(middleRow, m1.getDimensionX() - middleRow), m2);

            subTask1.fork();
            subTask2.fork();

            var result = new Matrix(m1.getDimensionX(), m2.getDimensionY());
            var subResult1 = subTask1.join();
            var subResult2 = subTask2.join();

            for (int i = 0; i < subResult1.getDimensionX(); i++) {
                result.data[i] = subResult1.data[i];
            }

            for (int i = 0; i < subResult2.getDimensionX(); i++) {
                result.data[middleRow + i] = subResult2.data[i];
            }
            return result;
        } else {

            Matrix result = new Matrix(m1.getDimensionX(), m2.getDimensionY());

            for(int i = 0; i < m1.getDimensionX(); i++) {
                for(int j = 0; j < m2.getDimensionY(); j++) {
                    for(int k = 0; k < m2.getDimensionX(); k++) {
                        result.data[i][j] += m1.get(i, k) * m2.get(k, j);
                    }
                }
            }
            return result;
        }
    }
}
