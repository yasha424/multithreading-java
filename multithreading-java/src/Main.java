import Matrix.*;

public class Main {

    static final int rowsA = 1000;
    static final int colsA = 1000;
    static final int colsB = 1000;

    public static void main(String[] args) {
        Matrix a = Matrix.generateRandomMatrix(rowsA, colsA);
        Matrix b = Matrix.generateRandomMatrix(colsA, colsB);

        long startTime = System.currentTimeMillis();
        Matrix c = a.multiply(b);
        long endTime = System.currentTimeMillis();

        System.out.println("Execution time: " + (endTime - startTime) + "ms");
    }
}
