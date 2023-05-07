package Models;

import java.util.Random;

public class Matrix {
    private final int rows;
    private final int columns;
    public double[][] data;

    /// Returns random generated matrix with a given size
    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.data = new double[rows][columns];

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                this.data[i][j] = 0.0;
            }
        }
    }

    /// Returns Identity matrix with a given size
    public Matrix(int size) {
        this.rows = size;
        this.columns = size;
        this.data = new double[size][size];

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                this.data[i][j] = i == j ? 1.0 : 0;
            }
        }
    }

    static public Matrix generateRandomMatrix(int rows, int columns) {
        Matrix matrix = new Matrix(rows, columns);

        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix.data[i][j] = random.nextDouble();
            }
        }

        return matrix;
    }

    static public Matrix generateRandomMatrix(int rows, int columns, double minValue, double maxValue) {
        Matrix matrix = new Matrix(rows, columns);

        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix.data[i][j] = minValue + (maxValue - minValue) * random.nextDouble();
            }
        }

        return matrix;
    }


    public double get(int x, int y) {
        return data[x][y];
    }

    public int getDimensionX() {
        return rows;
    }

    public int getDimensionY() {
        return columns;
    }

    public Matrix multiply(Matrix matrix) {
        if (this.getDimensionY() != matrix.getDimensionX()) {
            throw new IllegalArgumentException("Not right size for multiplication");
        }

        Matrix result = new Matrix(this.getDimensionX(), matrix.getDimensionY());

        for(int i = 0; i < this.getDimensionX(); i++) {
            for(int j = 0; j < matrix.getDimensionY(); j++) {
                for(int k = 0; k < matrix.getDimensionX(); k++) {
                    result.data[i][j] += this.get(i, k) * matrix.get(k, j);
                }
            }
        }

        return result;
    }

    public void add(Matrix matrix) {
        if (this.getDimensionX() != matrix.getDimensionX() ||
            this.getDimensionY() != matrix.getDimensionY()) {
            throw new IllegalArgumentException("Not right size for addition");
        }

        for(int i = 0; i < this.getDimensionX(); i++) {
            for(int j = 0; j < matrix.getDimensionY(); j++) {
                this.data[i][j] += matrix.data[i][j];
            }
        }
    }

    public Matrix transposed() {
        Matrix transposed = new Matrix(this.getDimensionY(), this.getDimensionX());

        for(int i = 0; i < this.getDimensionX(); i++) {
            for(int j = 0; j < this.getDimensionY(); j++) {
                transposed.data[j][i] = this.get(i, j);
            }
        }

        return transposed;
    }

    public Matrix getRows(int offset, int rows) {
        if (offset + rows > this.getDimensionX()) {
            throw new IllegalArgumentException("Out of bounds");
        }
        Matrix result = new Matrix(rows, this.getDimensionY());

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < this.getDimensionY(); j++) {
                result.data[i][j] = this.get(offset + i, j);
            }
        }
        return result;
    }

    public Boolean equals(Matrix matrix) {
        if (matrix.getDimensionX() != this.getDimensionX() ||
            matrix.getDimensionY() != this.getDimensionY()) {
            return false;
        }
        for(int i = 0; i < this.getDimensionX(); i++) {
            for(int j = 0; j < this.getDimensionY(); j++) {
                if (Math.abs(this.get(i, j) - matrix.get(i, j)) > 0.0000001) {
                    return false;
                }
            }
        }
        return true;
    }

    public void print() {
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                System.out.print(get(i, j) + " ");
            }
            System.out.println();
        }
    }

    public Matrix getSquareBlock(int fromRow, int fromColumn, int size) {
        Matrix matrix = new Matrix(size, size);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix.data[i][j] = this.get(fromRow + i, fromColumn + j);
            }
        }

        return matrix;
    }
}
