import java.util.Scanner;
import java.util.stream.IntStream;

public class MatrixUtil {

    public static void executeCommand(int command, Scanner scanner) {
        switch (command) {
            case 1:
                executeAddition(scanner);
                break;
            case 2:
                executeMultiplicationByScalar(scanner);
                break;
            case 3:
                executeMultiplicationByMatrix(scanner);
                break;
            case 4:
                executeTransposeCommand(scanner);
                break;
            case 5:
                executeCalculateDeterminantCommand(scanner);
                break;
            case 6:
                executeInverseMatrixCommand(scanner);
                break;
            case 0:
                System.exit(1);
                break;
            default:
                System.out.println("Wrong operation!");
        }
    }

    private static void executeInverseMatrixCommand(Scanner scanner) {
        System.out.println("Enter matrix size: > ");
        int rows = scanner.nextInt();
        int cols = scanner.nextInt();
        System.out.println("Enter matrix:");
        double[][] matrix = createMatrix(scanner, rows, cols);
        double determinant = calculateDeterminant(matrix);
        if (determinant == 0) {
            System.out.println("Matrix inverse can't be calculated. Determinant is 0");
            return;
        }
        double[][] coFactorMatrix = createCofactorMatrix(matrix);
        double[][] transposed = transposeByMainDiagonal(coFactorMatrix);
        double[][] inverseMatrix = createInverseMatrix(determinant, transposed);
        System.out.println("The result is:");
        printMatrix(inverseMatrix);
    }

    private static double[][] createInverseMatrix(double determinant, double[][] transposed) {
        double scalar = 1 / determinant;
        return multiplyByScalar(transposed, scalar);
    }

    private static double[][] createCofactorMatrix(double[][] matrix) {
        double[][] cofactorMatrix = new double[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                double cofactor = calculateCofactor(matrix, i, j);
                cofactorMatrix[i][j] = cofactor;
            }
        }
        return cofactorMatrix;
    }

    private static double calculateCofactor(double[][] matrix, int row, int column) {
        if (matrix.length > 3) {
            return Math.pow(-1, (row + 1) + (column + 1)) * calculateDeterminant(reduceMatrixByOneRowAndOneColumn(matrix, column, row));
        } else {
            return Math.pow(-1, (row + 1) + (column + 1)) * calculateMinor(reduceMatrixByOneRowAndOneColumn(matrix, column, row));
        }
    }

    private static double calculateMinor(double[][] matrix) {
        return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
    }


    private static void executeCalculateDeterminantCommand(Scanner scanner) {
        System.out.println("Enter matrix size: > ");
        int rows = scanner.nextInt();
        int cols = scanner.nextInt();
        System.out.println("Enter matrix:");
        double[][] matrix = createMatrix(scanner, rows, cols);
        double determinant = calculateDeterminant(matrix);
        System.out.println("The result is:");
        System.out.println(determinant);
    }

    private static double calculateDeterminant(double[][] matrix) {
        if (matrix.length == 2) {
            return calculateMinor(matrix);
        } else {
            double determinant = 0;
            for (int i = 0; i < matrix.length; i++) {
                if (i % 2 == 0) {
                    determinant += matrix[0][i] * calculateDeterminant(reduceMatrixByOneRowAndOneColumn(matrix, i, 0));
                } else {
                    determinant -= matrix[0][i] * calculateDeterminant(reduceMatrixByOneRowAndOneColumn(matrix, i, 0));
                }
            }
            return determinant;
        }
    }

    private static double[][] reduceMatrixByOneRowAndOneColumn(double[][] matrix, int column, int row) {
        double[][] reduced = new double[matrix.length - 1][matrix.length - 1];
        double[][] reducedByRow = new double[matrix.length - 1][matrix.length];

        //delete one row from matrix
        int pos = 0;
        for (int i = 0; i < matrix.length; i++) {
            if (i == row) continue;
            reducedByRow[pos] = matrix[i];
            pos++;
        }

        for (int i = 0; i < reducedByRow.length; i++) { //go over all rows of reduced by one row matrix
            double[] line = reducedByRow[i];
            double[] reducedRow = IntStream.range(0, line.length)
                    .filter(index -> index != column)
                    .mapToDouble(index -> line[index])
                    .toArray();
            reduced[i] = reducedRow;
        }
        return reduced;
    }

    private static double calculateDeterminantOfTripleMatrix(double[][] matrix) {
        return matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1]) -
                matrix[0][1] * (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0]) +
                matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]);
    }

    private static double[][] executeTransposeCommand(Scanner scanner) {
        System.out.println("1. Main diagonal");
        System.out.println("2. Side diagonal");
        System.out.println("3. Vertical line");
        System.out.println("4. Horizontal line");
        System.out.println("Your choice: > ");
        int choice = scanner.nextInt();
        System.out.println("Enter matrix size: > ");
        int rows = scanner.nextInt();
        int cols = scanner.nextInt();
        System.out.println("Enter matrix:");
        double[][] matrix = createMatrix(scanner, rows, cols);
        double[][] transposed;
        switch (choice) {
            case 1:
                transposed = transposeByMainDiagonal(matrix);
                break;
            case 2:
                transposed = transposeBySideDiagonal(matrix);
                break;
            case 3:
                transposed = transposeByVerticalLine(matrix);
                break;
            case 4:
                transposed = transposeByHorizontalLine(matrix);
                break;
            default:
                transposed = null;
                System.out.println("ERROR");
        }
        printMatrix(transposed);
        return transposed;
    }

    private static double[][] transposeByHorizontalLine(double[][] matrix) {
        double[][] transposed = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix[0].length; i++) {
            int index = matrix.length;
            for (int j = 0; j < matrix.length; j++) {
                transposed[j][i] = matrix[index - 1][i];
                index--;
            }
        }
        return transposed;
    }

    private static double[][] transposeByVerticalLine(double[][] matrix) {
        double[][] transposed = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            int length = matrix[0].length;
            int index = length;
            double[] reversed = new double[length];
            for (int j = 0; j < length; j++) {
                reversed[index - 1] = matrix[i][j];
                index--;
            }
            transposed[i] = reversed;
        }
        return transposed;
    }

    private static double[][] transposeBySideDiagonal(double[][] matrix) {
        int index = matrix[0].length - 1;
        for (int i = 0; i < index; i++) {
            for (int j = 0; j < index - i; j++) {
                double tmp = matrix[i][j];
                matrix[i][j] = matrix[index - j][index - i];
                matrix[index - j][index - i] = tmp;
            }
        }
        return matrix;
    }

    private static double[][] transposeByMainDiagonal(double[][] matrix) {
        double[][] transposed = new double[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix[0].length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                transposed[i][j] = matrix[j][i];
            }
        }
        return transposed;
    }

    private static double[][] executeMultiplicationByMatrix(Scanner scanner) {
        System.out.println("Enter size of first matrix: > ");
        int rowsA = scanner.nextInt();
        int colsA = scanner.nextInt();
        System.out.println("Enter first matrix: ");
        double[][] matrixA = MatrixUtil.createMatrix(scanner, rowsA, colsA);

        System.out.println("Enter size of second matrix: > ");
        int rowsB = scanner.nextInt();
        int colsB = scanner.nextInt();

        System.out.println("Enter second matrix: ");
        double[][] matrixB = MatrixUtil.createMatrix(scanner, rowsB, colsB);

        double[][] multiplied = MatrixUtil.multiplyByMatrix(matrixA, matrixB);
        printMatrix(multiplied);

        return multiplied;
    }

    private static double[][] executeMultiplicationByScalar(Scanner scanner) {
        System.out.println("Enter size of matrix: > ");
        int rows = scanner.nextInt();
        int cols = scanner.nextInt();
        System.out.println("Enter matrix: ");

        double[][] matrix = MatrixUtil.createMatrix(scanner, rows, cols);

        System.out.println("Enter constant: > ");
        double constant = scanner.nextDouble();

        double[][] multiplied = MatrixUtil.multiplyByScalar(matrix, constant);
        printMatrix(multiplied);

        return multiplied;
    }

    private static double[][] executeAddition(Scanner scanner) {
        System.out.println("Enter size of first matrix: > ");
        int rowsA = scanner.nextInt();
        int colsA = scanner.nextInt();

        System.out.println("Enter first matrix: ");
        double[][] matrixA = MatrixUtil.createMatrix(scanner, rowsA, colsA);

        System.out.println("Enter size of second matrix: > ");
        int rowsB = scanner.nextInt();
        int colsB = scanner.nextInt();

        System.out.println("Enter second matrix: ");
        double[][] matrixB = MatrixUtil.createMatrix(scanner, rowsB, colsB);

        double[][] added = MatrixUtil.add(matrixA, matrixB);
        printMatrix(added);

        return added;
    }

    private static double[][] createMatrix(Scanner scanner, int rows, int cols) {
        double[][] matrix = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            double[] row = new double[cols];
            for (int j = 0; j < cols; j++) {
                row[j] = scanner.nextDouble();
                matrix[i] = row;
            }
        }
        return matrix;
    }

    private static double[][] add(double[][] a, double[][] b) {
        if (a.length != b.length || a[0].length != b[0].length) {
            System.out.println("ERROR");
            return null;
        }
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                a[i][j] += b[i][j];
            }
        }
        return a;
    }

    private static double[][] multiplyByScalar(double[][] a, double scalar) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                a[i][j] *= scalar;
            }
        }
        return a;
    }

    private static double[][] multiplyByMatrix(double[][] a, double[][] b) {
        int matrixBRows = b.length;
        int matrixARows = a.length;
        int matrixBCols = b[0].length;
        if (a[0].length != matrixBRows) {
            System.out.println("ERROR");
            return null;
        }
        double[][] c = new double[matrixARows][matrixBCols];
        for (int i = 0; i < matrixARows; i++) {
            for (int j = 0; j < matrixBCols; j++) {
                double sum = 0;
                for (int k = 0; k < matrixBRows; k++) {
                    sum += a[i][k] * b[k][j];
                }
                c[i][j] = sum;
            }
        }
        return c;
    }

    private static void printMatrix(double[][] matrix) {
        if (matrix != null) {
            for (double[] doubles : matrix) {
                for (double aDouble : doubles) {
                    System.out.printf("%.2f ", aDouble);
                }
                System.out.println();
            }
        }
    }
}