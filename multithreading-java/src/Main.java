import Algorithms.Fox.FoxMatrixMultiplicator;
import Algorithms.Stripe.StripeMatrixMultiplicator;
import Models.*;

public class Main {
    public static void main(String[] args) {
        final Matrix m1 = Matrix.generateRandomMatrix(1000, 1000);
        final Matrix m2 = Matrix.generateRandomMatrix(1000, 1000);

        long start = System.currentTimeMillis();
        MatrixMultiplicator matrixMultiplicator1 = new StripeMatrixMultiplicator(8);
        Result result1 = matrixMultiplicator1.multiply(m1, m2);
        long end = System.currentTimeMillis();

        System.out.println("Stripe 2 threaded: " + (end - start));

        start = System.currentTimeMillis();
        MatrixMultiplicator matrixMultiplicator2 = new FoxMatrixMultiplicator(8);
        Result result2 = matrixMultiplicator2.multiply(m1, m2);
        end = System.currentTimeMillis();

        System.out.println("Fox 2 threaded: " + (end - start));
        System.out.println(result1.equals(result2));
    }

    static public Matrix oneThreadedMultiplication(Matrix m1, Matrix m2) {
        return m1.multiply(m2);
    }

    static public Matrix multiThreadedStripeMultiplication(Matrix m1, Matrix m2) {
        StripeMatrixMultiplicator multiplicator = new StripeMatrixMultiplicator(8);

        return multiplicator.multiply(m1, m2);
    }
}
