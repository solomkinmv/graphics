package io.github.solomkinmv.graphics.lab2.figures;

import io.github.solomkinmv.graphics.lab2.graphics.Transformer;
import io.github.solomkinmv.graphics.lab2.types.Point3D;
import io.github.solomkinmv.graphics.lab2.types.Triangle;
import io.github.solomkinmv.graphics.lab2.types.Vector3D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class ZBufferedImage {

    private static final int NORMAL_LENGTH = 10;
    private final BufferedImage image;
    private final Triangle[] triangles;
    private final int width;
    private final int height;
    private final boolean showNormal;
    private final boolean showGrid;
    private final Transformer transformer;
    private double[] zBuffer;
    private Color[] colors;

    public ZBufferedImage(Triangle[] triangles, int height, int width, int rotate, int roll, int pitch, boolean showNormal, boolean showGrid) {
        this.triangles = triangles;
        this.width = width;
        this.height = height;
        this.showNormal = showNormal;
        this.showGrid = showGrid;
        transformer = new Transformer(rotate, roll, pitch);
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        initZBuffer();

        generateImage();
    }

    private void initZBuffer() {
        zBuffer = new double[height * width];
        colors = new Color[height * width];

        for (int q = 0; q < zBuffer.length; q++) {
            zBuffer[q] = Double.NEGATIVE_INFINITY;
        }
    }

    private void drawNormal(Triangle triangle) {
        Vector3D n = triangle.normal();
        Point3D center = triangle.center();
        Point3D vp = new Point3D(center.x - n.x * NORMAL_LENGTH, center.y - n.y * NORMAL_LENGTH,
                                 center.z - n.z * NORMAL_LENGTH);
        markLine(center, vp, triangle, Color.red);

    }

    private void processTriangle(Triangle triangle) {
        triangle = transformer.transform(triangle);
        triangle = triangle.add(width / 2., height / 2., 0);

        for (int y = triangle.minY(); y <= triangle.maxY(); y++) {
            for (int x = triangle.minX(); x <= triangle.maxX(); x++) {
                if (triangle.containsPoint(x, y)) {
                    markPoint(x, y, triangle, triangle.color);
                }
                if (showGrid) {
                    markLine(triangle.v1, triangle.v2, triangle, Color.black);
                    markLine(triangle.v1, triangle.v3, triangle, Color.black);
                    markLine(triangle.v2, triangle.v3, triangle, Color.black);
                }
                if (showNormal) {
                    drawNormal(triangle);
                }
            }
        }
    }

    private void markLine(Point3D p1, Point3D p2, Triangle triangle, Color color) {
        int x1 = p1.getX();
        int x2 = p2.getX();
        int y1 = p1.getY();
        int y2 = p2.getY();

        int w = x2 - x1;
        int h = y2 - y1;
        int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
        if (w < 0) dx1 = -1;
        else if (w > 0) dx1 = 1;
        if (h < 0) dy1 = -1;
        else if (h > 0) dy1 = 1;
        if (w < 0) dx2 = -1;
        else if (w > 0) dx2 = 1;
        int longest = Math.abs(w);
        int shortest = Math.abs(h);
        if (!(longest > shortest)) {
            longest = Math.abs(h);
            shortest = Math.abs(w);
            if (h < 0) dy2 = -1;
            else if (h > 0) dy2 = 1;
            dx2 = 0;
        }
        int numerator = longest >> 1;
        for (int i = 0; i <= longest; i++) {
            markPoint(x1, y1, triangle, color);
            numerator += shortest;
            if (!(numerator < longest)) {
                numerator -= longest;
                x1 += dx1;
                y1 += dy1;
            } else {
                x1 += dx2;
                y1 += dy2;
            }
        }
    }

    private void markPoint(int x, int y, Triangle triangle, Color color) {
        double depth = triangle.depth(x, y);
        int zIndex = y * width + x;
        if (x <= width && x >= 0 && y <= height && y >= 0 && zBuffer[zIndex] <= depth) {
            colors[zIndex] = color;
            zBuffer[zIndex] = depth;
        }
    }

    private void normalizeColorsAndDrawPoints() {
        double depthMin = Double.MAX_VALUE, depthMax = Double.MIN_VALUE;
        for (int i = 0; i < colors.length; i++) {
            if (colors[i] == null) {
                continue;
            }

            double depth = zBuffer[i];
            if (depth < depthMin) depthMin = depth;
            if (depth > depthMax) depthMax = depth;
        }

        double bottomShift = 0 - depthMin;
        double topBoundary = depthMax + bottomShift;

        for (int i = 0; i < colors.length; i++) {
            if (colors[i] == null) {
                continue;
            }

            if (Color.black.equals(colors[i])) {
                image.setRGB(i % width, i / width, colors[i].getRGB());
                continue;
            }

            int color = (int) ((zBuffer[i] + bottomShift) * 255 / topBoundary);
            image.setRGB(i % width, i / width, new Color(color, color, color).getRGB());
        }
    }

    private void generateImage() {
        Arrays.stream(triangles).forEach(this::processTriangle);
        normalizeColorsAndDrawPoints();
    }

    public Image get() {
        return image;
    }
}
