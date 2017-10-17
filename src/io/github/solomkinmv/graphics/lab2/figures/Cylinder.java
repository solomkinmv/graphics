package io.github.solomkinmv.graphics.lab2.figures;

import io.github.solomkinmv.graphics.lab2.generator.CylinderPoints;
import io.github.solomkinmv.graphics.lab2.graphics.Graphics;
import io.github.solomkinmv.graphics.lab2.graphics.IsometricTransformer;
import io.github.solomkinmv.graphics.lab2.points.Point2D;
import io.github.solomkinmv.graphics.lab2.points.Point3D;

import java.util.stream.DoubleStream;

public class Cylinder implements Drawing {
    private final Graphics graphics;
    private final IsometricTransformer isometricTransformer;

    public Cylinder(Graphics graphics) {
        this(graphics, 225, 60);
    }

    public Cylinder(Graphics graphics, int fiAngle, int thetaAngle) {
        this.graphics = graphics;
        isometricTransformer = new IsometricTransformer(fiAngle, thetaAngle);
    }

    @Override
    public void draw() {
        drawAxis();
        drawFigure();
    }

    private void drawFigure() {
        Point3D[][] cylinderPoints = new CylinderPoints(200, 300, 50, 20).generatePoints();

        for (int i = 0; i < cylinderPoints.length; i++) {
            for (int j = 0; j < cylinderPoints[i].length; j++) {
                Point3D point3D = cylinderPoints[i][j];
                Point2D transoformedPoint = isometricTransformer.transform(point3D);
                int jMax = cylinderPoints[i].length - 1;

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
        int len = 300;
        Point3D ox = new Point3D(len, 0, 0);
        Point3D oy = new Point3D(0, len, 0);
        Point3D oz = new Point3D(0, 0, len);

        graphics.line(isometricTransformer.transform(ox));
        graphics.line(isometricTransformer.transform(oy));
        graphics.line(isometricTransformer.transform(oz));
    }

    private Point2D[] generateCircle(int z) {
        double parts = 100;
        double step = 1 / parts;
        int scale = 100;
        return DoubleStream.iterate(0, (v) -> v <= 1, (v) -> v + step)
                           .mapToObj(u -> create3dPointFromSeed(u, z, scale))
                           .map(isometricTransformer::transform)
                           .toArray(Point2D[]::new);
    }

    private Point3D create3dPointFromSeed(double u, int z, int scale) {
        return new Point3D(Math.cos(getRad(u)) * scale, Math.sin(getRad(u)) * scale, z);
    }

    private double getRad(double u) {
        return 2 * Math.PI * u;
    }
}
