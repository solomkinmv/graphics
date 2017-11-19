package io.github.julia.graphics.lab2.figures;

import io.github.julia.graphics.lab2.types.Point3D;
import io.github.julia.graphics.lab2.types.Triangle;

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
                    Point3D v1 = points[i][j];
                    Point3D v2 = points[i + 1][(j + 1) % jMax];
                    Point3D v3 = points[i + 1][j];
                    Point3D v4 = points[i][(j + 1) % jMax];

                    triangles.add(new Triangle(v3, v1, v2));
                    triangles.add(new Triangle(v1, v4, v2));
                }
            }
        }

        return triangles.toArray(new Triangle[triangles.size()]);
    }
}
