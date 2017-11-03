package io.github.solomkinmv.graphics.lab2.graphics;

import io.github.solomkinmv.graphics.lab2.types.Matrix;
import io.github.solomkinmv.graphics.lab2.types.Point3D;

public class Transformer {
    private final double rotate;
    private final double heading;
    private final double pitch;
    private final Matrix transformationMatrix;

    public Transformer(int rotate, int heading, int pitch) {
        this.rotate = Math.toRadians(rotate);
        this.heading = Math.toRadians(heading);
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
                {Math.cos(heading), 0, Math.sin(heading)},
                {0, 1, 0},
                {-Math.sin(heading), 0, Math.cos(heading)}
        });
    }

    public Point3D transform(Point3D point) {
        return transformationMatrix.transform(point);
    }
}
