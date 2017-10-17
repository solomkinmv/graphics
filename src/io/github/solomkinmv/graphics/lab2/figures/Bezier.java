package io.github.solomkinmv.graphics.lab2.figures;

import io.github.solomkinmv.graphics.lab2.generator.BezierPoints;
import io.github.solomkinmv.graphics.lab2.graphics.Graphics;
import io.github.solomkinmv.graphics.lab2.graphics.IsometricTransformer;
import io.github.solomkinmv.graphics.lab2.types.Point2D;
import io.github.solomkinmv.graphics.lab2.types.Point3D;
import io.github.solomkinmv.graphics.lab2.types.Vector;

import java.awt.*;
import java.util.List;

public class Bezier implements Drawing {

    private static final int NORMAL_LENGTH = 40;
    private static final int AXIS_LENGTH = 600;
    private final List<Point2D> sourcePoints;
    private final Graphics graphics;
    private final IsometricTransformer isometricTransformer;
    private final int hParts;
    private final int vParts;
    private final int radius;
    private final int height;
    private final boolean showNormal;

    public Bezier(List<Point2D> sourcePoints, Graphics graphics, int fiAngle, int thetaAngle, int hParts, int vParts, int radius, int height) {
        this(sourcePoints, graphics, fiAngle, thetaAngle, hParts, vParts, radius, height, false);
    }

    public Bezier(List<Point2D> sourcePoints, Graphics graphics, int fiAngle, int thetaAngle, int hParts, int vParts, int radius, int height, boolean showNormal) {
        this.sourcePoints = sourcePoints;
        this.graphics = graphics;
        this.hParts = hParts;
        this.vParts = vParts;
        this.radius = radius;
        this.height = height;
        this.showNormal = showNormal;
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
                    graphics.line(transoformedPoint,
                                  isometricTransformer.transform(cylinderPoints[i + 1][(j + 1) % jMax]));
                    if (showNormal) {
                        drawNormal(point3D, cylinderPoints[i + 1][j], cylinderPoints[i][(j + 1) % jMax]);
                    }
                }
            }
        }
    }

    private void drawNormal(Point3D a, Point3D b, Point3D c) {
        Vector v1 = new Vector(a, b);
        Vector v2 = new Vector(a, c);

        Vector n = v1.normal(v2);
        Point3D vp = new Point3D(a.x - n.x * NORMAL_LENGTH, a.y - n.y * NORMAL_LENGTH, a.z - n.z * NORMAL_LENGTH);
        graphics.line(isometricTransformer.transform(a), isometricTransformer.transform(vp), Color.red);
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
