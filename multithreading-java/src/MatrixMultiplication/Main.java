package MatrixMultiplication;

import MatrixMultiplication.MatrixMultiplicationTask.MatrixMultiplicationTask;
import MatrixMultiplication.Models.Matrix;
import MatrixMultiplication.Stripe.StripeMatrixMultiplicator;

import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        var m1 = Matrix.generateRandomMatrix(1000, 1000);
        var m2 = Matrix.generateRandomMatrix(1000, 1000);


        var mult2 = new StripeMatrixMultiplicator(2);
        long start = System.currentTimeMillis();
//        var result2 = m1.multiply(m2);
        var result2 = mult2.multiply(m1, m2);
        long end = System.currentTimeMillis();
        System.out.println("Stripe execution time: " + (end - start));

        var multiplicationTask = new MatrixMultiplicationTask(m1, m2, 100);
        var forkJoinPool = new ForkJoinPool(4);
        start = System.currentTimeMillis();
        var result1 = forkJoinPool.invoke(multiplicationTask);
        end = System.currentTimeMillis();
        System.out.println("ForkJoin execution time: " + (end - start));

        System.out.println(result2.equals(result1));


//        result2.print();
//        result1.print();
    }
}
