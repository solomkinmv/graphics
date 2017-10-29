package io.github.solomkinmv.graphics.lab2.figures;

import io.github.solomkinmv.graphics.lab2.generator.PointsGenerator;
import io.github.solomkinmv.graphics.lab2.graphics.Graphics;
import io.github.solomkinmv.graphics.lab2.graphics.IsometricTransformer;
import io.github.solomkinmv.graphics.lab2.types.Point2D;
import io.github.solomkinmv.graphics.lab2.types.Point3D;
import io.github.solomkinmv.graphics.lab2.types.Triangle;
import io.github.solomkinmv.graphics.lab2.types.Vector3D;

import java.awt.*;

public class WireframeDrawing implements Drawing {

    private static final int AXIS_LENGTH = 600;
    private static final int NORMAL_LENGTH = 40;
    private final Graphics graphics;
    private final IsometricTransformer isometricTransformer;
    private final boolean showNormal;
    private final Triangle[] triangles;

    public WireframeDrawing(Graphics graphics, PointsGenerator pointsGenerator, int fiAngle, int thetaAngle, boolean showNormal, boolean isContinuous) {
        this.graphics = graphics;
        this.isometricTransformer = new IsometricTransformer(fiAngle, thetaAngle);
        this.showNormal = showNormal;
        triangles = new TrianglesCreator().createFromGrid(pointsGenerator.generatePoints(), isContinuous);
    }


    @Override
    public void draw() {
        drawAxis();
        drawFigure();
    }

    private void drawFigure() {

        for (Triangle triangle : triangles) {
            graphics.line(isometricTransformer.transform(triangle.v1), isometricTransformer.transform(triangle.v2));
            graphics.line(isometricTransformer.transform(triangle.v1), isometricTransformer.transform(triangle.v3));
            graphics.line(isometricTransformer.transform(triangle.v2), isometricTransformer.transform(triangle.v3));
            if (showNormal) {
                drawNormal(triangle);
            }
        }
    }

    private void drawNormal(Triangle triangle) {
        Vector3D n = triangle.normal();
        Point3D center = triangle.center();
        Point3D vp = new Point3D(center.x - n.x * NORMAL_LENGTH, center.y - n.y * NORMAL_LENGTH, center.z - n.z * NORMAL_LENGTH);
        graphics.line(isometricTransformer.transform(center), isometricTransformer.transform(vp), Color.red);
    }

    // todo: move to abstract class
    private void drawAxis() {
        Point3D ox = new Point3D(AXIS_LENGTH, 0, 0);
        Point3D oy = new Point3D(0, AXIS_LENGTH, 0);
        Point3D oz = new Point3D(0, 0, AXIS_LENGTH);

        graphics.line(isometricTransformer.transform(ox));
        graphics.line(isometricTransformer.transform(oy));
        graphics.line(isometricTransformer.transform(oz));
    }
}
