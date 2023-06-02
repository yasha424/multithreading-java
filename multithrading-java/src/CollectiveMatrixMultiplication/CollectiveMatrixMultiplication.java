package CollectiveMatrixMultiplication;

import mpi.MPI;
import Matrix.Matrix;

public class CollectiveMatrixMultiplication {

    final static int MASTER = 0;
    final static int NUM_ROWS_A = 1500;
    final static int NUM_COLS_A = 1500;
    final static int NUM_COLS_B = 1500;

    public static void main(String[] args) {
        MPI.Init(args);

        int numTasks = MPI.COMM_WORLD.Size();
        int taskId = MPI.COMM_WORLD.Rank();

        Matrix a = new Matrix(NUM_ROWS_A, NUM_COLS_A);
        Matrix b = new Matrix(NUM_COLS_A, NUM_COLS_B);
        Matrix c = new Matrix(NUM_ROWS_A, NUM_COLS_B);

        long startTime = System.currentTimeMillis();
        if (taskId == MASTER) {
            a = Matrix.lowerTriangleMatrix(NUM_ROWS_A, NUM_COLS_A, 1);
            b = Matrix.lowerTriangleMatrix(NUM_COLS_A, NUM_COLS_B, 1);
        }

        int numRowsInTask = NUM_ROWS_A / numTasks;
        int extraRows = NUM_ROWS_A % numTasks;
        int[] numRowsInTasks = new int[numTasks];
        int[] offsetsInTasks = new int[numTasks];

        for (int i = 0; i < numTasks; i++) {
            numRowsInTasks[i] = (i < extraRows) ? numRowsInTask + 1 : numRowsInTask;

            if (i + 1 < numTasks) {
                offsetsInTasks[i + 1] = offsetsInTasks[i] + numRowsInTasks[i];
            }
        }

        Matrix aRowsBuffer = new Matrix(numRowsInTasks[taskId], NUM_COLS_A);
        MPI.COMM_WORLD.Scatterv(
                a.data, 0, numRowsInTasks, offsetsInTasks,
                MPI.OBJECT,
                aRowsBuffer.data, 0, numRowsInTasks[taskId],
                MPI.OBJECT,
                MASTER
        );

        MPI.COMM_WORLD.Bcast(b.data, 0, NUM_COLS_A, MPI.OBJECT, MASTER);

        Matrix cRowsBuffer = new Matrix(numRowsInTasks[taskId], NUM_COLS_B);

        for (int i = 0; i < numRowsInTasks[taskId]; i++) {
            for (int j = 0; j < NUM_COLS_B; j++) {
                for (int k = 0; k < NUM_COLS_A; k++) {
                    cRowsBuffer.data[i][j] += aRowsBuffer.data[i][k] * b.data[k][j];
                }
            }
        }

//        MPI.COMM_WORLD.Gatherv(
//                cRowsBuffer.data,
//                0, numRowsInTasks[taskId],
//                MPI.OBJECT,
//                c.data,
//                0, numRowsInTasks, offsetsInTasks,
//                MPI.OBJECT,
//                MASTER
//        );

        MPI.COMM_WORLD.Allgatherv(
                cRowsBuffer.data,
                0, numRowsInTasks[taskId],
                MPI.OBJECT,
                c.data,
                0, numRowsInTasks, offsetsInTasks,
                MPI.OBJECT
        );


        if (taskId == MASTER) {
            long endTime = System.currentTimeMillis();
            System.out.println("Execution time: " + (endTime - startTime) + "ms");

            var valRes = a.multiply(b);
            System.out.println(valRes.equals(c));
        }

        MPI.Finalize();
    }
}