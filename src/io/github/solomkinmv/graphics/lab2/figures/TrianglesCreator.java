package io.github.solomkinmv.graphics.lab2.figures;

import io.github.solomkinmv.graphics.lab2.types.Point3D;
import io.github.solomkinmv.graphics.lab2.types.Triangle;

import java.util.ArrayList;

public class TrianglesCreator {
    public Triangle[] createFromGrid(Point3D[][] points, boolean continuous) {
        ArrayList<Triangle> triangles = new ArrayList<>();

        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                int jMax = points[i].length - 1;
                int iMax = points.length - 1;

                boolean hasSidePoint = continuous || j + 1 < jMax;
                boolean hasTopPoint = i < iMax;
                if (hasTopPoint && hasSidePoint) {
                    triangles.add(new Triangle(points[i][j], points[i + 1][j], points[i + 1][(j + 1) % jMax]));
                    triangles.add(new Triangle(points[i][j], points[i][(j + 1) % jMax], points[i + 1][(j + 1) % jMax]));
                }
            }
        }

        return triangles.toArray(new Triangle[triangles.size()]);
    }
}
