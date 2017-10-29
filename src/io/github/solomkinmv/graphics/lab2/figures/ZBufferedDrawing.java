package io.github.solomkinmv.graphics.lab2.figures;

import io.github.solomkinmv.graphics.lab2.generator.PointsGenerator;
import io.github.solomkinmv.graphics.lab2.graphics.Graphics;
import io.github.solomkinmv.graphics.lab2.graphics.IsometricTransformer;
import io.github.solomkinmv.graphics.lab2.types.*;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class ZBufferedDrawing implements Drawing {
    private static final int SIZE = 1000;
    private final boolean showNormal;
    private final IsometricTransformer isometricTransformer;
    private final Graphics graphics;
    private final Triangle[] triangles;
    private final double[][] zbuffer;

    public ZBufferedDrawing(Graphics graphics, PointsGenerator pointsGenerator, int fiAngle, int thetaAngle, boolean showNormal, boolean isContinuous) {
        this.graphics = graphics;
        this.isometricTransformer = new IsometricTransformer(fiAngle, thetaAngle);
        this.showNormal = showNormal;
        triangles = new TrianglesCreator().createFromGrid(pointsGenerator.generatePoints(), isContinuous);
        zbuffer = new double[SIZE][SIZE]; // todo: get real size
    }

    private boolean pointWithinTriangle(Triangle2D triangle, Point2D rayOrigin) {
//        graphics.line(triangle.v1, triangle.v2);
//        graphics.line(triangle.v1, triangle.v3);
//        graphics.line(triangle.v3, triangle.v2);
//        graphics.dot(rayOrigin);
//        graphics.line(rayOrigin, new Point2D(rayOrigin.x + 10, rayOrigin.y + 10), Color.red);
        Vector2D v0 = new Vector2D(triangle.v3, triangle.v1);
        Vector2D v1 = new Vector2D(triangle.v2, triangle.v1);
        Vector2D v2 = new Vector2D(rayOrigin, triangle.v1);
        double d00 = v0.dotProduct(v0);
        double d01 = v0.dotProduct(v1);
        double d02 = v0.dotProduct(v2);
        double d11 = v1.dotProduct(v1);
        double d12 = v1.dotProduct(v2);

        double invDenom = 1.0 / (d00 * d11 - d01 * d01);
        double u = (d11 * d02 - d01 * d12) * invDenom;
        double v = (d00 * d12 - d01 * d02) * invDenom;

        // Check if point is in triangle

//        if (u == 0 || v == 0) {
//            zbuffer[rayOrigin.getX() + SIZE / 2][rayOrigin.getY() + SIZE / 2] = Double.MAX_VALUE;
//            graphics.dot(rayOrigin);
//        }

        return (u >= 0) && (v >= 0) && ((u + v) <= 1);
    }

    private boolean inside(Triangle2D triangle2D, Point2D rayOrigin) {
        int x0 = rayOrigin.getX();
        int y0 = rayOrigin.getY();
        int x1 = triangle2D.v1.getX();
        int x2 = triangle2D.v2.getX();
        int x3 = triangle2D.v3.getX();
        int y1 = triangle2D.v1.getY();
        int y2 = triangle2D.v2.getY();
        int y3 = triangle2D.v3.getY();

        int a = (x1 - x0) * (y2 - y1) - (x2 - x1) * (y1 - y0);
        int b = (x2 - x0) * (y3 - y2) - (x3 - x2) * (y2 - y0);
        int c = (x3 - x0) * (y1 - y3) - (x1 - x3) * (y3 - y0);

//        if (a == 0 || b == 0 || c == 0) {
//            zbuffer[rayOrigin.getX() + SIZE / 2][rayOrigin.getY() + SIZE / 2] = Double.MAX_VALUE;
//            graphics.dot(rayOrigin);
//        }

        return (a >= 0 && b >= 0 && c >= 0) || (a <= 0 && b <= 0 && c <= 0);
    }

    private double zValueOfPoint(Triangle triangle, Point2D rayOrigin) {
        Vector3D surfaceNormal = triangle.normal();
        double A = surfaceNormal.x;
        double B = surfaceNormal.y;
        double C = surfaceNormal.z;
        if (C == 0) return Double.MIN_VALUE;
        double dzdx = -A / C;
        double dzdy = -B / C;
        return triangle.v1.z +
                dzdx * (rayOrigin.x - isometricTransformer.transform(triangle.v1).x)
                + dzdy * (rayOrigin.y - isometricTransformer.transform(triangle.v1).y);
    }

    public static double[] createPlane(Point3D p1, Point3D p2, Point3D p3) {
        return createPlane(p1.x, p2.x, p3.x, p1.y, p2.y, p3.y, p1.z, p2.z, p3.z);
    }

    public static double[] createPlane(double x1, double x2, double x3,
                                       double y1, double y2, double y3,
                                       double z1, double z2, double z3) {
        double[] plane = new double[4];

        //A
        plane[0] = y1*(z2 -z3) + y2*(z3 -z1) + y3*(z1 -z2);

        //B
        plane[1] = z1*(x2 -x3) + z2*(x3 -x1) + z3*(x1 -x2);

        //C
        plane[2] = x1*(y2 -y3) + x2*(y3 -y1) + x3*(y1 -y2);

        //D
        plane[3] = -( x1*(y2*z3 -y3*z2) + x2*(y3*z1 -y1*z3) + x3*(y1*z2 -y2*z1));

        return plane;
    }

    public static double resolveZ(Point2D rayOrigin, double[] plane) {
        if (plane[2] == 0) {
            return Double.MAX_VALUE;
        }

        return - (plane[0] * rayOrigin.x + plane[1] * rayOrigin.y + plane[3]) / plane[2];
    }

    public static double calculateZ(Triangle triangle, Point2D rayOrigin) {
        double[] plane = createPlane(triangle.v1, triangle.v2, triangle.v3);
        return resolveZ(rayOrigin, plane);
    }

    private void drawFigure() {
        Color[] colors = {Color.green, Color.red, Color.yellow, Color.blue, Color.pink, Color.gray, Color.cyan, Color.black, Color.magenta, Color.orange};
        Random random = new Random();
        for (Triangle triangle3D : triangles) {
            Triangle2D triangle = transform(triangle3D);
            Color color = colors[random.nextInt(colors.length)];
//            drawTriangle(triangle);
            for (int y = triangle.minY(); y < triangle.maxY(); y += 1) {
                for (int x = triangle.minX(); x < triangle.maxX(); x += 1) {
                    Point2D rayOrigin = new Point2D(x, y);
                    if (inside(triangle, rayOrigin)) {
//                        double zDepth = 1;
                        double zDepth = zValueOfPoint(triangle3D, rayOrigin);
                        if (zDepth > zbuffer[x + SIZE / 2][y + SIZE / 2]) {
                            zbuffer[x + SIZE / 2][y + SIZE / 2] = zDepth;
                            graphics.line(rayOrigin, rayOrigin, color);
                        }
                    }
                }
            }
        }
    }

    private void drawTriangle(Triangle2D triangle) {
        graphics.line(triangle.v1, triangle.v2);
        graphics.line(triangle.v1, triangle.v3);
        graphics.line(triangle.v3, triangle.v2);
    }

    private Triangle2D transform(Triangle triangle) {
        return new Triangle2D(isometricTransformer.transform(triangle.v1),
                              isometricTransformer.transform(triangle.v2),
                              isometricTransformer.transform(triangle.v3));
    }

    @Override
    public void draw() {
        drawFigure();
    }
}
