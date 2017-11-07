package io.github.solomkinmv.graphics.lab2.generator;

import io.github.solomkinmv.graphics.lab2.figures.TrianglesCreator;
import io.github.solomkinmv.graphics.lab2.types.Point3D;
import io.github.solomkinmv.graphics.lab2.types.Triangle;

public class PortionPolygonsGenerator implements PolygonsGenerator {

    private final Point3D[] line1;
    private final Point3D[] line2;
    private final int hParts;
    private final int vParts;

    public PortionPolygonsGenerator(Point3D[] line1, Point3D[] line2, int hParts, int vParts) {
        this.line1 = line1;
        this.line2 = line2;
        this.hParts = hParts;
        this.vParts = vParts;
    }

    @Override
    public Triangle[] generate() {
        return new TrianglesCreator()
                .createFromGrid(new PortionPoints(line1, line2, hParts, vParts).generatePoints(), false);
    }
}
