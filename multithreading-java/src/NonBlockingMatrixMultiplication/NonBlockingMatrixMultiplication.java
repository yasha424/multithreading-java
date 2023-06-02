package NonBlockingMatrixMultiplication;

import mpi.MPI;
import mpi.Request;
import Matrix.Matrix;

import java.util.ArrayList;

public class NonBlockingMatrixMultiplication {

    final static int MASTER = 0;
    final static int NUM_ROWS_A = 500;
    final static int NUM_COLS_A = 500;
    final static int NUM_COLS_B = 500;
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

        int[] tasksOffsets = new int[numWorkers];
        int[] tasksNumRows = new int[numWorkers];

        if (taskId == MASTER) {
            int extraRows = NUM_ROWS_A % numWorkers;
            int numRowsInTask = a.getDimensionX() / numWorkers;

            a = Matrix.generateRandomMatrix(NUM_ROWS_A, NUM_COLS_A);
            b = Matrix.generateRandomMatrix(NUM_COLS_A, NUM_COLS_B);

            long startTime = System.currentTimeMillis();

            for (int destination = 1; destination <= numWorkers; destination++) {
                tasksNumRows[destination - 1] = (destination <= extraRows) ? numRowsInTask + 1 : numRowsInTask;

                MPI.COMM_WORLD.Isend(tasksNumRows, destination - 1, 1, MPI.INT, destination, FROM_MASTER_TAG);
                MPI.COMM_WORLD.Isend(a.data, tasksOffsets[destination - 1], tasksNumRows[destination - 1], MPI.OBJECT, destination, FROM_MASTER_TAG);
                MPI.COMM_WORLD.Isend(b.data, 0, NUM_COLS_A, MPI.OBJECT, destination, FROM_MASTER_TAG);

                if (destination < numWorkers) {
                    tasksOffsets[destination] += tasksOffsets[destination - 1] + tasksNumRows[destination - 1];
                }
            }

            ArrayList<Request> requests = new ArrayList<>();
            for (int source = 1; source <= numWorkers; source++) {
                var req = MPI.COMM_WORLD.Irecv(c.data, tasksOffsets[source - 1], tasksNumRows[source - 1], MPI.OBJECT, source, FROM_WORKER_TAG);
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

            MPI.COMM_WORLD.Irecv(tasksNumRows, taskId - 1, 1, MPI.INT, MASTER, FROM_MASTER_TAG).Wait();

            Request aReq = MPI.COMM_WORLD.Irecv(a.data, 0, tasksNumRows[taskId - 1], MPI.OBJECT, MASTER, FROM_MASTER_TAG);
            Request bReq = MPI.COMM_WORLD.Irecv(b.data, 0, NUM_COLS_A, MPI.OBJECT, MASTER, FROM_MASTER_TAG);

            aReq.Wait();
            bReq.Wait();

            for (int i = 0; i < tasksNumRows[taskId - 1]; i++) {
                for (int j = 0; j < NUM_COLS_B; j++) {
                    for (int k = 0; k < NUM_COLS_A; k++) {
                        c.data[i][j] += a.data[i][k] * b.data[k][j];
                    }
                }
            }

            MPI.COMM_WORLD.Isend(c.data, 0, tasksNumRows[taskId - 1], MPI.OBJECT, MASTER, FROM_WORKER_TAG);
        }

        MPI.Finalize();
    }
}