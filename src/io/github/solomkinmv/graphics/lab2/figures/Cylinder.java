package io.github.solomkinmv.graphics.lab2.figures;

import io.github.solomkinmv.graphics.lab2.graphics.Graphics;
import io.github.solomkinmv.graphics.lab2.graphics.IsometricTransformer;
import io.github.solomkinmv.graphics.lab2.points.Point2D;
import io.github.solomkinmv.graphics.lab2.points.Point3D;

import java.util.stream.DoubleStream;

public class Cylinder implements Drawing {
    private final Graphics graphics;
    private final IsometricTransformer isometricTransformer;
    private static final int HEIGHT = 200;

    public Cylinder(Graphics graphics) {
        this.graphics = graphics;
        isometricTransformer = new IsometricTransformer();
    }

    @Override
    public void draw() {
        drawAxis();
        drawCircle(0);
        drawCircle(HEIGHT);
        drawSides();
    }

    private void drawSides() {
        Point2D[] point2DS = generateCircle(0);
        double shift = 0;
        Point2D max = null;
        for (Point2D point2D : point2DS) {
            if (point2D.x > shift) {
                shift = point2D.x;
                max = point2D;
            }
        }
        graphics.line(max, new Point2D(max.x, max.y + HEIGHT));
        graphics.line(new Point2D(-max.x, max.y), new Point2D(-max.x, max.y + HEIGHT));
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

    private void drawCircle(int z) {
        Point2D[] point2DS = generateCircle(z);
        for (int i = 0; i < point2DS.length - 1; i++) {
            graphics.line(point2DS[i], point2DS[i + 1]);
        }
        graphics.line(point2DS[0], point2DS[point2DS.length - 1]);
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
