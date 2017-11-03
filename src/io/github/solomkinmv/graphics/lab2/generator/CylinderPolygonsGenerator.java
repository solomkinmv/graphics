package io.github.solomkinmv.graphics.lab2.generator;

import io.github.solomkinmv.graphics.lab2.figures.TrianglesCreator;
import io.github.solomkinmv.graphics.lab2.types.Triangle;

public class CylinderPolygonsGenerator implements PolygonsGenerator {

    private final int r;
    private final int h;
    private final int hParts;
    private final int vParts;

    public CylinderPolygonsGenerator(int r, int h, int hParts, int vParts) {
        this.r = r;
        this.h = h;
        this.hParts = hParts;
        this.vParts = vParts;
    }

    @Override
    public Triangle[] generate() {
        return new TrianglesCreator()
                .createFromGrid(
                        new CylinderPoints(r, h, hParts, vParts).generatePoints(),
                        true);
    }
}
