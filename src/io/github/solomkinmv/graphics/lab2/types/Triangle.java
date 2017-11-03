package io.github.solomkinmv.graphics.lab2.types;

import java.awt.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Triangle {
    public final Point3D v1;
    public final Point3D v2;
    public final Point3D v3;
    public final Color color;

    public Triangle(Point3D v1, Point3D v2, Point3D v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        color = Color.white;
    }

    public Triangle(Point3D v1, Point3D v2, Point3D v3, Color color) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.color = color;
    }

    public Vector3D normal() {
        Vector3D vector1 = new Vector3D(v1, v2);
        Vector3D vector2 = new Vector3D(v1, v3);
        return vector1.normal(vector2);
    }

    public Point3D center() {
        Point3D m = new Point3D((v2.x + v3.x) / 2, (v2.y + v3.y) / 2, (v2.z + v3.z) / 2);
        return new Point3D((v1.x + 2 * m.x) / 3, (v1.y + 2 * m.y) / 3, (v1.z + 2 * m.z) / 3);
    }

    public double area() {
        return (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);
    }

    public Triangle add(double xDelta, double yDelta, double zDelta) {
        return new Triangle(v1.add(xDelta, yDelta, zDelta),
                            v2.add(xDelta, yDelta, zDelta),
                            v3.add(xDelta, yDelta, zDelta),
                            color);
    }

    public int minX() {
        return minByAxis(Point3D::getX);
    }

    public int minY() {
        return minByAxis(Point3D::getY);
    }

    public int minZ() {
        return minByAxis(Point3D::getZ);
    }

    public int maxX() {
        return maxByAxis(Point3D::getX);
    }

    public int maxY() {
        return maxByAxis(Point3D::getY);
    }

    public int maxZ() {
        return maxByAxis(Point3D::getZ);
    }

    private int minByAxis(Function<Point3D, Integer> toAxisValue) {
        return minMaxByAxis(Math::min, toAxisValue);
    }

    private int maxByAxis(Function<Point3D, Integer> toAxisValue) {
        return minMaxByAxis(Math::max, toAxisValue);
    }

    private int minMaxByAxis(BiFunction<Integer, Integer, Integer> minMax, Function<Point3D, Integer> toAxisValue) {
        return minMax.apply(toAxisValue.apply(v1), minMax.apply(toAxisValue.apply(v2), toAxisValue.apply(v3)));
    }

    public boolean containsPoint(Point3D point) {
        return containsPoint(point.x, point.y);
    }

    public boolean containsPoint(double x, double y) {
        double triangleArea = area();

        double b1 = ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
        double b2 = ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
        double b3 = ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;

        return b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1;
    }

    public double depth(double x, double y) {
        double triangleArea = area();

        double b1 = ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
        double b2 = ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
        double b3 = ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;

        return b1 * v1.z + b2 * v2.z + b3 * v3.z;
    }

    public Color shade() {
        Vector3D norm = normal();

        double angleCos = Math.abs(norm.z);


        double redLinear = Math.pow(color.getRed(), 2.4) * angleCos;
        double greenLinear = Math.pow(color.getGreen(), 2.4) * angleCos;
        double blueLinear = Math.pow(color.getBlue(), 2.4) * angleCos;

        int red = (int) Math.pow(redLinear, 1 / 2.4);
        int green = (int) Math.pow(greenLinear, 1 / 2.4);
        int blue = (int) Math.pow(blueLinear, 1 / 2.4);

        return new Color(red, green, blue);
    }

    @Override
    public String toString() {
        return String.format("Vector[(%.2f, %.2f, %.2f), (%.2f, %.2f, %.2f), (%.2f, %.2f, %.2f)]", v1.x, v1.y, v1.z,
                             v2.x, v2.y, v2.z, v3.x, v3.y, v3.z);
    }
}
