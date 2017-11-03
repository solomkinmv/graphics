package io.github.solomkinmv.graphics.lab2.graphics;

import io.github.solomkinmv.graphics.lab2.types.Matrix;
import io.github.solomkinmv.graphics.lab2.types.Point3D;
import io.github.solomkinmv.graphics.lab2.types.Triangle;

public class Transformer {
    private final double rotate;
    private final double roll;
    private final double pitch;
    private final Matrix transformationMatrix;

    public Transformer(int rotate, int roll, int pitch) {
        this.rotate = Math.toRadians(rotate);
        this.roll = Math.toRadians(roll);
        this.pitch = Math.toRadians(pitch);
        transformationMatrix = rotateMatrix().multiply(pitchMatrix()).multiply(headingMatrix());
    }

    private Matrix rotateMatrix() {
        return new Matrix(new double[][]{
                {Math.cos(rotate), 0, -Math.sin(rotate)},
                {0, 1, 0},
                {Math.sin(rotate), 0, Math.cos(rotate)}
        });
    }

    private Matrix pitchMatrix() {
        return new Matrix(new double[][]{
                {1, 0, 0},
                {0, Math.cos(pitch), Math.sin(pitch)},
                {0, -Math.sin(pitch), Math.cos(pitch)}
        });
    }

    private Matrix headingMatrix() {
        return new Matrix(new double[][]{
                {Math.cos(roll), 0, Math.sin(roll)},
                {0, 1, 0},
                {-Math.sin(roll), 0, Math.cos(roll)}
        });
    }

    public Point3D transform(Point3D point) {
        return transformationMatrix.transform(point);
    }

    public Triangle transform(Triangle triangle) {
        return new Triangle(transformationMatrix.transform(triangle.v1),
                            transformationMatrix.transform(triangle.v2),
                            transformationMatrix.transform(triangle.v3), triangle.color);
    }
}
