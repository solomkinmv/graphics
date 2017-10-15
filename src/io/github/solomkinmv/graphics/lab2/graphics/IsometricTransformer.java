package io.github.solomkinmv.graphics.lab2.graphics;

import io.github.solomkinmv.graphics.lab2.points.Point2D;
import io.github.solomkinmv.graphics.lab2.points.Point3D;

public class IsometricTransformer {

    private final double angle;
    private final double[][] transformation;

    public IsometricTransformer() {
        angle = -Math.PI / 6;
        transformation = new double[][]{
                {
                        Math.cos(angle),
                        Math.sin(angle)
                },
                {
                        Math.cos(Math.PI - angle),
                        Math.sin(Math.PI - angle)
                }
        };
    }

    public Point2D transform(Point3D point) {
        return new Point2D(
                point.x * transformation[0][0] + point.y * transformation[1][0],
                point.x * transformation[0][1] + point.y * transformation[1][1] + point.z
        );

    }
}

