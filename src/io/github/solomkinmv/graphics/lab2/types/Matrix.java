package io.github.solomkinmv.graphics.lab2.types;

public class Matrix {
    private final double[][] values;

    public Matrix(double[][] values) {
        assert values.length == 3;
        assert values[0].length == 3;
        this.values = values;
    }

    public Matrix multiply(Matrix other) {
        int rows = other.values.length;
        int cols = other.values[0].length;

        assert rows == cols : "rows should be equal to cols";
        assert rows == 3;

        double[][] result = new double[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                for (int i = 0; i < 3; i++) {
                    result[row][col] += values[row][i] * other.values[i][col];
                }
            }
        }
        return new Matrix(result);
    }

    public Point3D transform(Point3D in) {
        return new Point3D(
                in.x * values[0][0] + in.y * values[1][0] + in.z * values[2][0],
                in.x * values[0][1] + in.y * values[1][1] + in.z * values[2][1],
                in.x * values[0][2] + in.y * values[1][2] + in.z * values[2][2]
        );
    }
}
