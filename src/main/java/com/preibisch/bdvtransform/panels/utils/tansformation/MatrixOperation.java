package com.preibisch.bdvtransform.panels.utils.tansformation;

public class MatrixOperation {
    public static double[][] multiplyMatrices(double[][] firstMatrix, double[][] secondMatrix) {
        double[][] result = new double[firstMatrix.length][secondMatrix[0].length];
        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[row].length; col++)
                result[row][col] = multiplyMatricesCell(firstMatrix, secondMatrix, row, col);
        }
        return result;
    }

    public static double multiplyMatricesCell(double[][] firstMatrix, double[][] secondMatrix, int row, int col) {
        double cell = 0;
        for (int i = 0; i < secondMatrix.length; i++)
            cell += firstMatrix[row][i] * secondMatrix[i][col];
        return cell;
    }

    public static double[] flatMatrix(double[][] matrix) {
        double[] result = new double[matrix.length * matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                result[i * matrix[0].length + j] = matrix[i][j];
        return result;
    }

    public static void print(double[] result) {
        for (double x : result)
            System.out.print(x + " ");
        System.out.println();
    }

    public static void print(double[][] matrix) {
        for (double[] list : matrix) {
            for (double e : list)
                System.out.print(e + " ");
            System.out.println();
        }
    }

    public static double[][] toMatrix(double[] list, int n) {
        double[][] result = new double[list.length / n][n];
        for (int i = 0; i < list.length / n; i++)
            for (int j = 0; j < n; j++)
                result[i][j] = list[i * n + j];
        return result;
    }

    public static void main(String[] args) {
        double[][] matrix = {
                new double[]{1d, 2d, 3d, 4d},
                new double[]{5d, 6d, 7d, 8d},
                new double[]{9d, 10d, 11d, 12d}
        };
        double[] result = flatMatrix(matrix);
        print(matrix);
        print(result);
        double[][] resultMat = toMatrix(result, 4);
        print(resultMat);
    }

    public static double[] reorderTranslation(double[] translation, int[] positions) {
        double[] result = new double[translation.length];
        for (int i = 0; i < translation.length; i++) {
            result[positions[i]] = translation[i];
        }
        return result;
    }

}
