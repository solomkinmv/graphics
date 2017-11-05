package io.github.solomkinmv.graphics.lab2.generator;

import io.github.solomkinmv.graphics.lab2.figures.TrianglesCreator;
import io.github.solomkinmv.graphics.lab2.types.Point2D;
import io.github.solomkinmv.graphics.lab2.types.Triangle;

import java.util.List;

public class BezierPolygonsGenerator implements PolygonsGenerator {
    private final int r;
    private final int h;
    private final int hParts;
    private final int vParts;
    private final List<Point2D> sourcePoints;

    public BezierPolygonsGenerator(int r, int h, int hParts, int vParts, List<Point2D> sourcePoints) {
        this.r = r;
        this.h = h;
        this.hParts = hParts;
        this.vParts = vParts;
        this.sourcePoints = sourcePoints;
    }

    @Override
    public Triangle[] generate() {
        return new TrianglesCreator()
                .createFromGrid(new BezierPoints(sourcePoints, r, h, hParts, vParts).generatePoints(),
                                true);
    }
}
