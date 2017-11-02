package io.github.solomkinmv.graphics.lab2.generator;

import io.github.solomkinmv.graphics.lab2.types.Point3D;
import io.github.solomkinmv.graphics.lab2.types.Triangle;

import java.awt.*;

public class TetrahedronPolygonsGenerator implements PolygonsGenerator {

    private final int edgeLength;

    public TetrahedronPolygonsGenerator(int edgeLength) {
        this.edgeLength = edgeLength;
    }

    @Override
    public Triangle[] generate() {
        return new Triangle[]{
                new Triangle(new Point3D(edgeLength, edgeLength, edgeLength),
                             new Point3D(-edgeLength, -edgeLength, edgeLength),
                             new Point3D(-edgeLength, edgeLength, -edgeLength),
                             Color.WHITE),
                new Triangle(new Point3D(edgeLength, edgeLength, edgeLength),
                             new Point3D(-edgeLength, -edgeLength, edgeLength),
                             new Point3D(edgeLength, -edgeLength, -edgeLength),
                             Color.RED),
                new Triangle(new Point3D(-edgeLength, edgeLength, -edgeLength),
                             new Point3D(edgeLength, -edgeLength, -edgeLength),
                             new Point3D(edgeLength, edgeLength, edgeLength),
                             Color.GREEN),
                new Triangle(new Point3D(-edgeLength, edgeLength, -edgeLength),
                             new Point3D(edgeLength, -edgeLength, -edgeLength),
                             new Point3D(-edgeLength, -edgeLength, edgeLength),
                             Color.BLUE)
        };
    }
}
