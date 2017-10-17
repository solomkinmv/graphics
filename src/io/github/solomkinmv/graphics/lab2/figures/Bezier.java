package io.github.solomkinmv.graphics.lab2.figures;

import io.github.solomkinmv.graphics.lab2.generator.BezierPoints;
import io.github.solomkinmv.graphics.lab2.graphics.Graphics;
import io.github.solomkinmv.graphics.lab2.graphics.IsometricTransformer;
import io.github.solomkinmv.graphics.lab2.points.Point2D;
import io.github.solomkinmv.graphics.lab2.points.Point3D;

import java.util.List;

public class Bezier implements Drawing {

    private static final int AXIS_LENGTH = 600;
    private final List<Point2D> sourcePoints;
    private final Graphics graphics;
    private final IsometricTransformer isometricTransformer;
    private final int hParts;
    private final int vParts;
    private final int radius;
    private final int height;

    public Bezier(List<Point2D> sourcePoints, Graphics graphics, int fiAngle, int thetaAngle, int hParts, int vParts, int radius, int height) {
        this.sourcePoints = sourcePoints;
        this.graphics = graphics;
        this.hParts = hParts;
        this.vParts = vParts;
        this.radius = radius;
        this.height = height;
        isometricTransformer = new IsometricTransformer(fiAngle, thetaAngle);
    }

    @Override
    public void draw() {
        drawAxis();
        drawFigure();
    }

    private void drawFigure() {
        Point3D[][] cylinderPoints = new BezierPoints(sourcePoints, radius, height, hParts, vParts).generatePoints();

        for (int i = 0; i < cylinderPoints.length; i++) {
            for (int j = 0; j < cylinderPoints[i].length; j++) {
                Point3D point3D = cylinderPoints[i][j];
                Point2D transoformedPoint = isometricTransformer.transform(point3D);
                int jMax = cylinderPoints[i].length;

                graphics.line(transoformedPoint, isometricTransformer.transform(cylinderPoints[i][(j + 1) % jMax]));
                int iMax = cylinderPoints.length - 1;
                boolean hasTopPoint = i < iMax;
                if (hasTopPoint) {
                    graphics.line(transoformedPoint, isometricTransformer.transform(cylinderPoints[i + 1][j]));
                }
                if (hasTopPoint) {
                    graphics.line(transoformedPoint,
                                  isometricTransformer.transform(cylinderPoints[i + 1][(j + 1) % jMax]));
                }
            }
        }
    }

    private void drawAxis() {
        Point3D ox = new Point3D(AXIS_LENGTH, 0, 0);
        Point3D oy = new Point3D(0, AXIS_LENGTH, 0);
        Point3D oz = new Point3D(0, 0, AXIS_LENGTH);

        graphics.line(isometricTransformer.transform(ox));
        graphics.line(isometricTransformer.transform(oy));
        graphics.line(isometricTransformer.transform(oz));
    }
}
