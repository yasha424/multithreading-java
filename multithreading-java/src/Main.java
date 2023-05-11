import Algorithms.Fox.FoxMatrixMultiplicator;
import Algorithms.Stripe.StripeMatrixMultiplicator;
import Models.*;

public class Main {

    public static void main(String[] args) {
        final int[] sizes = { 32, 64, 128, 256, 512, 1000 };
        final int[] threadNums = { 1, 2, 4, 6, 8, 16, 32, 64, 100, 400, 625 };

//        testDifferentSizesOfMatrices(sizes);
        testDifferentCountOfThreads(threadNums, 1000);
    }

    public static void testDifferentSizesOfMatrices(int[] sizes) {
        double[] naiveMetrics = new double[sizes.length];
        double[] stripeMetrics = new double[sizes.length];
        double[] foxMetrics = new double[sizes.length];
        long start, end;

        for (int i = 0; i < sizes.length; i++) {

            Matrix m1 = Matrix.generateRandomMatrix(sizes[i], sizes[i]);
            Matrix m2 = Matrix.generateRandomMatrix(sizes[i], sizes[i]);

            start = System.currentTimeMillis();
            Matrix naiveResult = m1.multiply(m2);
            end = System.currentTimeMillis();
            System.out.println("Naive multiplication with size of " + sizes[i] + "x" + sizes[i] + ": "  + (end - start));
            naiveMetrics[i] = end - start;

            start = System.currentTimeMillis();
            MatrixMultiplicator stripeMultiplicator = new StripeMatrixMultiplicator(16);
            Result stripeResult = stripeMultiplicator.multiply(m1, m2);
            end = System.currentTimeMillis();
            System.out.println("Stripe multiplication with size of " + sizes[i] + "x" + sizes[i] + ": " + (end - start));
            stripeMetrics[i] = end - start;

            start = System.currentTimeMillis();
            MatrixMultiplicator foxMultiplicator = new FoxMatrixMultiplicator(4);
            Result foxResult = foxMultiplicator.multiply(m1, m2);
            end = System.currentTimeMillis();
            System.out.println("Fox multiplication with size of " + sizes[i] + "x" + sizes[i] + ": " + (end - start));
            foxMetrics[i] = end - start;

//            System.out.println(naiveResult.equals(stripeResult));
//            System.out.println(naiveResult.equals(foxResult));
            System.out.println();
        }
    }


    public static void testDifferentCountOfThreads(int[] threadNums, int sizeOfMatrix) {
        double[] naiveMetrics = new double[threadNums.length];
        double[] stripeMetrics = new double[threadNums.length];
        double[] foxMetrics = new double[threadNums.length];
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
            MatrixMultiplicator foxMultiplicator = new FoxMatrixMultiplicator((int) Math.sqrt(threadNums[i]));
            Result foxResult = foxMultiplicator.multiply(m1, m2);
            end = System.currentTimeMillis();
            System.out.println("Fox multiplication with " + threadNums[i] + " threads: " + (end - start));
            foxMetrics[i] = end - start;

//            System.out.println(naiveResult.equals(stripeResult));
//            System.out.println(naiveResult.equals(foxResult));
        }

        for (int i = 0; i < threadNums.length; i++) {
            System.out.println("Speedup with " + threadNums[i] + " threads");
            System.out.println("\tStripe speedup: " + naiveMetrics[i] / stripeMetrics[i]);
            System.out.println("\tFox speedup: " + naiveMetrics[i] / foxMetrics[i]);
        }
    }


}
