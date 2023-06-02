package BlockingMatrixMultiplication;

import mpi.*;
import Matrix.Matrix;

public class BlockingMatrixMultiplication {

    final static int MASTER = 0;
    final static int NUM_ROWS_A = 800;
    final static int NUM_COLS_A = 800;
    final static int NUM_COLS_B = 800;
    final static int FROM_MASTER_TAG = 0;
    final static int FROM_WORKER_TAG = 1;

    public static void main(String[] args) {
        MPI.Init(args);

        int numTasks = MPI.COMM_WORLD.Size();
        int numWorkers = numTasks - 1;
        int taskId = MPI.COMM_WORLD.Rank();

        if (numTasks < 2) {
            System.out.println("Need at least two MPI tasks. Quitting...");
            MPI.COMM_WORLD.Abort(1);
            return;
        }

        Matrix a = new Matrix(NUM_ROWS_A, NUM_COLS_A);
        Matrix b = new Matrix(NUM_COLS_A, NUM_COLS_B);
        Matrix c = new Matrix(NUM_ROWS_A, NUM_COLS_B);

        int[] offset = {0};
        int[] rows = {0};

        if (taskId == MASTER) {
//            a = Matrix.lowerTriangleMatrix(NUM_ROWS_A, NUM_COLS_A, 1);
//            b = Matrix.lowerTriangleMatrix(NUM_COLS_A, NUM_COLS_B, 1);
            a = Matrix.generateRandomMatrix(NUM_ROWS_A, NUM_COLS_A);
            b = Matrix.generateRandomMatrix(NUM_COLS_A, NUM_COLS_B);

            long startTime = System.currentTimeMillis();

            int extraRows = NUM_ROWS_A % numWorkers;
            int numRowsInTask = a.getDimensionX() / numWorkers;

            for (int destination = 1; destination <= numWorkers; destination++) {
                rows[0] = (destination <= extraRows) ? numRowsInTask + 1 : numRowsInTask;

                MPI.COMM_WORLD.Send(offset, 0, 1, MPI.INT, destination, FROM_MASTER_TAG);
                MPI.COMM_WORLD.Send(rows, 0, 1, MPI.INT, destination, FROM_MASTER_TAG);
                MPI.COMM_WORLD.Send(a.data, offset[0], rows[0], MPI.OBJECT, destination, FROM_MASTER_TAG);
                MPI.COMM_WORLD.Send(b.data, 0, NUM_COLS_A, MPI.OBJECT, destination, FROM_MASTER_TAG);

                offset[0] += rows[0];
            }

            for (int source = 1; source <= numWorkers; source++) {
                MPI.COMM_WORLD.Recv(offset, 0, 1, MPI.INT, source, FROM_WORKER_TAG);
                MPI.COMM_WORLD.Recv(rows, 0, 1, MPI.INT, source, FROM_WORKER_TAG);
                MPI.COMM_WORLD.Recv(c.data, offset[0], rows[0], MPI.OBJECT, source, FROM_WORKER_TAG);
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Execution time: " + (endTime - startTime) + "ms");

            var valRes = a.multiply(b);
            System.out.println(valRes.equals(c));

        } else {
            MPI.COMM_WORLD.Recv(offset, 0, 1, MPI.INT, MASTER, FROM_MASTER_TAG);
            MPI.COMM_WORLD.Recv(rows, 0, 1, MPI.INT, MASTER, FROM_MASTER_TAG);
            MPI.COMM_WORLD.Recv(a.data, 0, rows[0], MPI.OBJECT, MASTER, FROM_MASTER_TAG);
            MPI.COMM_WORLD.Recv(b.data, 0, NUM_COLS_A, MPI.OBJECT, MASTER, FROM_MASTER_TAG);

            for (int i = 0; i < rows[0]; i++) {
                for (int j = 0; j < NUM_COLS_B; j++) {
                    for (int k = 0; k < NUM_COLS_A; k++) {
                        c.data[i][j] += a.data[i][k] * b.data[k][j];
                    }
                }
            }

            MPI.COMM_WORLD.Send(offset, 0, 1, MPI.INT, MASTER, FROM_WORKER_TAG);
            MPI.COMM_WORLD.Send(rows, 0, 1, MPI.INT, MASTER, FROM_WORKER_TAG);
            MPI.COMM_WORLD.Send(c.data, 0, rows[0], MPI.OBJECT, MASTER, FROM_WORKER_TAG);
        }

        MPI.Finalize();
    }
}