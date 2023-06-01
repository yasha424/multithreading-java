package NonBlockingMatrixMultiplication;

import mpi.*;
import Matrix.Matrix;

import java.util.ArrayList;

public class NonBlockingMatrixMultiplication {

    final static int MASTER = 0;
    final static int NUM_ROWS_A = 1000;
    final static int NUM_COLS_A = 1000;
    final static int NUM_COLS_B = 1000;
    final static int FROM_MASTER_TAG = 0;
    final static int FROM_WORKER_TAG = 1;

    public static void main(String[] args) {
        MPI.Init(args);

        int numTasks = MPI.COMM_WORLD.Size();
        int numWorkers = numTasks - 1; // 6
        int taskId = MPI.COMM_WORLD.Rank();

        if (numTasks < 2) {
            System.out.println("Need at least two MPI tasks. Quitting...");
            MPI.COMM_WORLD.Abort(1);
            return;
        }

        Matrix a = new Matrix(NUM_ROWS_A, NUM_COLS_A);
        Matrix b = new Matrix(NUM_COLS_A, NUM_COLS_B);
        Matrix c = new Matrix(NUM_ROWS_A, NUM_COLS_B);

        int[] offset = new int[numWorkers];
        int[] rows = new int[numWorkers];


        int extraRows = NUM_ROWS_A % numWorkers;
        int numRowsInTask = a.getDimensionX() / numWorkers;


        for (int destination = 1; destination <= numWorkers; destination++) {
            rows[destination - 1] = (destination <= extraRows) ? numRowsInTask + 1 : numRowsInTask;
        }

        for (int destination = 1; destination < numWorkers; destination++) {
            offset[destination] += rows[destination - 1];
        }

        if (taskId == MASTER) {
//            a = Matrix.lowerTriangleMatrix(NUM_ROWS_A, NUM_COLS_A, 1);
//            b = Matrix.lowerTriangleMatrix(NUM_COLS_A, NUM_COLS_B, 1);
            a = Matrix.generateRandomMatrix(NUM_ROWS_A, NUM_COLS_A);
            b = Matrix.generateRandomMatrix(NUM_COLS_A, NUM_COLS_B);

            long startTime = System.currentTimeMillis();


//                Request offsetReq = MPI.COMM_WORLD.Isend(offset, destination - 1, 1, MPI.INT, destination, FROM_MASTER_TAG);
//                Request rowsReq = MPI.COMM_WORLD.Isend(rows, destination - 1, 1, MPI.INT, destination, FROM_MASTER_TAG);
            for (int destination = 1; destination <= numWorkers; destination++) {
                MPI.COMM_WORLD.Isend(a.data, offset[destination - 1], rows[destination - 1], MPI.OBJECT, destination, FROM_MASTER_TAG);
                MPI.COMM_WORLD.Isend(b.data, 0, NUM_COLS_A, MPI.OBJECT, destination, FROM_MASTER_TAG);

//                offsetReq.Wait();
//                rowsReq.Wait();

            }

            ArrayList<Request> requests = new ArrayList<>();
            for (int source = 1; source <= numWorkers; source++) {
//                MPI.COMM_WORLD.Irecv(offset, 0, 1, MPI.INT, source, FROM_WORKER_TAG);//.Wait();
//                MPI.COMM_WORLD.Irecv(rows, 0, 1, MPI.INT, source, FROM_WORKER_TAG);//.Wait();
                var req = MPI.COMM_WORLD.Irecv(c.data, offset[source - 1], rows[source - 1], MPI.OBJECT, source, FROM_WORKER_TAG);
                requests.add(req);
            }

            for (var request : requests) {
                request.Wait();
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Execution time: " + (endTime - startTime) + "ms");

            var valRes = a.multiply(b);
            System.out.println(valRes.equals(c));

        } else {
//            MPI.COMM_WORLD.Irecv(offset, 0, 1, MPI.INT, MASTER, FROM_MASTER_TAG);//.Wait();
//            MPI.COMM_WORLD.Irecv(rows, 0, 1, MPI.INT, MASTER, FROM_MASTER_TAG);//.Wait();
            MPI.COMM_WORLD.Irecv(a.data, offset[taskId - 1], rows[taskId - 1], MPI.OBJECT, MASTER, FROM_MASTER_TAG).Wait();
            MPI.COMM_WORLD.Irecv(b.data, 0, NUM_COLS_A, MPI.OBJECT, MASTER, FROM_MASTER_TAG).Wait();

            for (int i = 0; i < rows[taskId - 1]; i++) {
                for (int j = 0; j < NUM_COLS_B; j++) {
                    for (int k = 0; k < NUM_COLS_A; k++) {
                        c.data[offset[taskId - 1] + i][j] += a.data[offset[taskId - 1] + i][k] * b.data[k][j];
                    }
                }
            }

//            MPI.COMM_WORLD.Isend(offset, 0, 1, MPI.INT, MASTER, FROM_WORKER_TAG);
//            MPI.COMM_WORLD.Isend(rows, 0, 1, MPI.INT, MASTER, FROM_WORKER_TAG);
            MPI.COMM_WORLD.Isend(c.data, offset[taskId - 1], rows[taskId - 1], MPI.OBJECT, MASTER, FROM_WORKER_TAG);
        }

        MPI.Finalize();
    }
}