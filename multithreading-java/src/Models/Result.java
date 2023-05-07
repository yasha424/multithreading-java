package Models;

public class Result extends Matrix {

    public Result(int rows, int columns) {
        super(rows, columns);
    }

    public void joinBlocks(Matrix[][] blocks) {
        final int blockSize = blocks[0][0].getDimensionX();
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                Matrix subMatrix = blocks[i][j];
                int subMatrixStartRow = i * blockSize;
                int subMatrixStartColumn = j * blockSize;
                for (int k = 0; k < blockSize && k + subMatrixStartRow < this.getDimensionX(); k++) {
                    for (int l = 0; l < blockSize && l + subMatrixStartColumn < this.getDimensionY(); l++) {
                        int row = k + subMatrixStartRow;
                        int column = l + subMatrixStartColumn;
                        this.data[row][column] = subMatrix.data[k][l];
                    }
                }
            }
        }
    }
}