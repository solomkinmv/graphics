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

        for (int q = 0; q < zBuffer.length; q++) {
            zBuffer[q] = Double.NEGATIVE_INFINITY;
        }
    }

    private void drawNormal(Triangle triangle) {
        Vector3D n = triangle.normal();
        Point3D center = triangle.center();
        Point3D vp = new Point3D(center.x - n.x * NORMAL_LENGTH, center.y - n.y * NORMAL_LENGTH, center.z - n.z * NORMAL_LENGTH);
        drawLine(center, vp, triangle, Color.red);

    }

    private void processTriangle(Triangle triangle) {
        triangle = transformer.transform(triangle);
        triangle = triangle.add(width / 2., height / 2., 0);

        for (int y = triangle.minY(); y <= triangle.maxY(); y++) {
            for (int x = triangle.minX(); x <= triangle.maxX(); x++) {
                if (triangle.containsPoint(x, y)) {
                    drawPoint(x, y, triangle, triangle.shade());
                }
                if (showGrid) {
                    drawLine(triangle.v1, triangle.v2, triangle, Color.black);
                    drawLine(triangle.v1, triangle.v3, triangle, Color.black);
                    drawLine(triangle.v2, triangle.v3, triangle, Color.black);
                }
                if (showNormal) {
                    drawNormal(triangle);
                }
            }
        }
    }

    private void drawLine(Point3D p1, Point3D p2, Triangle triangle, Color color) {
        int x1 = p1.getX();
        int x2 = p2.getX();
        int y1 = p1.getY();
        int y2 = p2.getY();

        double dx = Math.abs(x2 - x1);
        double dy = Math.abs(y2 - y1);


        if (x1 > x2)
        {
            int tmp = x1;
            x1 = x2;
            x2 = tmp;
            tmp = y1;
            y1 = y2;
            y2 = tmp;
        }

        if (x1 == x2) {
            drawHorizontalLine(x1, Math.min(y1, y2), Math.max(y1, y2), triangle, color);
            return;
        }

        if (y1 == y2) {
            drawVerticalLine(x1, x2, y1, triangle, color);
            return;
        }

        int sy = (y2 >= y1) ? 1 : -1; // direction for y

        double deltaerror = Math.abs(dy / dx); // Assume deltax != 0 (line is not vertical),
        // note that this division needs to be done in a way that preserves the fractional part
        double error = 0; // no error at start

        int y = y1;
        for (int x = x1; x <= x2; x++)
        {
            drawPoint(x, y, triangle, color);
            error += deltaerror;
            if (error >= 0.5)
            {
                y += sy;
                error -= 1;
            }
        }
    }

    private void drawVerticalLine(int x1, int x2, int y, Triangle triangle, Color color) {
        for (int x = x1; x <= x2; x++) {
            drawPoint(x, y, triangle, color);
        }
    }

    private void drawHorizontalLine(int x, int y1, int y2, Triangle triangle, Color color) {
        for (int y = y1; y <= y2; y++) {
            drawPoint(x, y, triangle, color);
        }
    }

    private void drawPoint(int x, int y, Triangle triangle, Color color) {
        double depth = triangle.depth(x, y);
        int zIndex = y * width + x;
        if (zBuffer[zIndex] < depth && x <= width && x >= 0 && y <= height && y >= 0) {
            image.setRGB(x, y, color.getRGB());
            zBuffer[zIndex] = depth;
        }
    }

    private void generateImage() {
        Arrays.stream(triangles).forEach(this::processTriangle);
    }

    public Image get() {
        return image;
    }
}
