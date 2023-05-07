import Algorithms.Fox.FoxMatrixMultiplicator;
import Algorithms.Stripe.StripeMatrixMultiplicator;
import Models.*;

public class Main {
    public static void main(String[] args) {
        final int[] threadNums = { 1, 2, 4, 6, 8, 16, 32, 64, 100, 400, 625 };
        testDifferentCountOfThreads(threadNums, 1000);
    }

    public static void testDifferentCountOfThreads(int[] threadNums, int sizeOfMatrix) {
        double naiveMetrics[] = new double[threadNums.length];
        double stripeMetrics[] = new double[threadNums.length];
        double foxMetrics[] = new double[threadNums.length];
        long start, end;

        for (int i = 0; i < threadNums.length; i++) {

            Matrix m1 = Matrix.generateRandomMatrix(sizeOfMatrix, sizeOfMatrix);
            Matrix m2 = Matrix.generateRandomMatrix(sizeOfMatrix, sizeOfMatrix);

            start = System.currentTimeMillis();
            Matrix naiveResult = m1.multiply(m2);
            end = System.currentTimeMillis();
            System.out.println("Naive multiplication: " + (end - start));
            naiveMetrics[i] = end - start;

            start = System.currentTimeMillis();
            MatrixMultiplicator stripeMultiplicator = new StripeMatrixMultiplicator(threadNums[i]);
            Result stripeResult = stripeMultiplicator.multiply(m1, m2);
            end = System.currentTimeMillis();
            System.out.println("Stripe multiplication with " + threadNums[i] + " threads: " + (end - start));
            stripeMetrics[i] = end - start;

            start = System.currentTimeMillis();
            System.out.println((int) Math.sqrt(threadNums[i]));
            MatrixMultiplicator foxMultiplicator = new FoxMatrixMultiplicator((int) Math.sqrt(threadNums[i]));
            Result foxResult = foxMultiplicator.multiply(m1, m2);
            end = System.currentTimeMillis();
            System.out.println("Fox multiplication with " + threadNums[i] + " threads: " + (end - start));
            foxMetrics[i] = end - start;

            System.out.println(naiveResult.equals(stripeResult));
            System.out.println(naiveResult.equals(foxResult));
        }

        for (int i = 0; i < threadNums.length; i++) {
            System.out.println("Speedup with " + threadNums[i] + " threads");
            System.out.println("\tStripe speedup: " + naiveMetrics[i] / stripeMetrics[i]);
            System.out.println("\tFox speedup: " + naiveMetrics[i] / foxMetrics[i]);
        }
    }


}
