package io.github.solomkinmv.graphics.lab2.generator;

import io.github.solomkinmv.graphics.lab2.points.Point2D;
import io.github.solomkinmv.graphics.lab2.points.Point3D;

import java.util.List;
import java.util.stream.DoubleStream;

public class BezierPoints implements PointsGenerator {
    private final List<Point2D> sourcePoints;
    private final int r;
    private final int h;
    private final int hParts;
    private final int vParts;

    public BezierPoints(List<Point2D> sourcePoints, int r, int h, int hParts, int vParts) {
        this.sourcePoints = sourcePoints;
        this.r = r;
        this.h = h;
        this.hParts = hParts;
        this.vParts = vParts;
    }

    public BezierPoints(List<Point2D> sourcePoints, int r, int h) {
        this(sourcePoints, r, h, 100, 10);
    }

    @Override
    public Point3D[][] generatePoints() {
        double step = 1. / vParts;

        return DoubleStream.iterate(0, (v) -> v <= 1, (v) -> v + step)
                           .mapToObj(this::generatePointsLayer)
                           .toArray(Point3D[][]::new);
    }

    private double getRad(double u) {
        return 2 * Math.PI * u;
    }

    private Point3D[] generatePointsLayer(double v) {
        double step = 1. / hParts;
        Point2D point2D = calculateBezierFunction(v);

        return DoubleStream.iterate(0, (u) -> u <= 1, (u) -> u + step)
                           .mapToObj(u -> getPoint(u, v, point2D))
                           .toArray(Point3D[]::new);
    }

    private Point3D getPoint(double u, double v, Point2D point2D) {
        return new Point3D(r * Math.cos(getRad(u)) * point2D.y, r * Math.sin(getRad(u)) * point2D.y, h * point2D.x);
    }

    private Point2D calculateBezierFunction(double t) {
        double x = 0;
        double y = 0;

        int n = sourcePoints.size() - 1;
        for (int i = 0; i <= n; i++) {
            x += fact(n) / (fact(i) * fact(n - i)) * sourcePoints.get(i).getX() * Math.pow(t, i) * Math.pow(1 - t,
                                                                                                            n - i);
            y += fact(n) / (fact(i) * fact(n - i)) * sourcePoints.get(i).getY() * Math.pow(t, i) * Math.pow(1 - t,
                                                                                                            n - i);
        }
        return new Point2D((int) x * h, (int) y * r);
    }

    private double fact(double arg) {
        if (arg < 0) throw new RuntimeException("negative argument.");
        if (arg == 0) return 1;

        double rezult = 1;
        for (int i = 1; i <= arg; i++)
            rezult *= i;
        return rezult;
    }
}