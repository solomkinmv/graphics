package io.github.solomkinmv.graphics.lab2.figures;

import io.github.solomkinmv.graphics.lab2.types.NormalizedTriangle;
import io.github.solomkinmv.graphics.lab2.types.Point3D;
import io.github.solomkinmv.graphics.lab2.types.Triangle;
import io.github.solomkinmv.graphics.lab2.types.Vector3D;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class NormalizedTrianglesCreator {
    public NormalizedTriangle[] createFromGrid(Point3D[][] points, boolean continuous) {
        List<List<Triangle>> triangles = new ArrayList<>();

        for (int i = 0; i < points.length; i += 2) {
            triangles.add(new ArrayList<>());
            triangles.add(new ArrayList<>());
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

                    triangles.get(i).add(new Triangle(v3, v1, v2));
                    triangles.get(i + 1).add(new Triangle(v1, v4, v2));
                }
            }
        }

        return normalizeTriangles(triangles, continuous);
    }

    private NormalizedTriangle[] normalizeTriangles(List<List<Triangle>> triangles, boolean continuous) {
        List<NormalizedTriangle> normalizedTriangles = new ArrayList<>();

        int rows = triangles.size();
        for (int i = 0; i < rows; i++) {
            int cols = triangles.get(i).size();

            for (int j = 0; j < cols; j++) {
                Triangle triangle = triangles.get(i).get(j);
                if (i % 2 == 0) { // top
                    Vector3D n1 = averageVector(getAdjacentNormals(triangles, continuous, i, j, continuous || j > 0, false, i < rows - 1, false, rows, cols));
                    Vector3D n2 = averageVector(getAdjacentNormals(triangles, continuous, i, j, continuous || j > 0, false, false, i > 0, rows, cols));
                    Vector3D n3 = averageVector(getAdjacentNormals(triangles, continuous, i, j, false, continuous || j < cols - 1, i < rows - 1, false, rows, cols));
                    normalizedTriangles.add(new NormalizedTriangle(triangle.v1, triangle.v2, triangle.v3, triangle.color, n1, n2, n3));
                } else { // bottom
                    Vector3D n1 = averageVector(getAdjacentNormals(triangles, continuous, i, j, continuous || j > 0, false, false, i > 0, rows, cols));
                    Vector3D n2 = averageVector(getAdjacentNormals(triangles, continuous, i, j, false, continuous || j < cols - 1, false, i > 0, rows, cols));
                    Vector3D n3 = averageVector(getAdjacentNormals(triangles, continuous, i, j, false, continuous || j < cols - 1, i < rows - 1, false, rows, cols));
                    normalizedTriangles.add(new NormalizedTriangle(triangle.v1, triangle.v2, triangle.v3, triangle.color, n1, n2, n3));
                }
            }
        }

        return normalizedTriangles.toArray(new NormalizedTriangle[normalizedTriangles.size()]);
    }

    private List<Vector3D> getAdjacentNormals(List<List<Triangle>> triangles, boolean continuous, int i, int j, boolean left, boolean right, boolean top, boolean bottom, int rows, int cols) {
        List<Vector3D> adjacentNormals = new ArrayList<>();
        adjacentNormals.add(triangles.get(i).get(j).normal());
        if (left) {
            adjacentNormals.add(triangles.get(i).get((j - 1) %  cols).normal());
        }

        if (right) {
            adjacentNormals.add(triangles.get(i).get((j + 1) %  cols).normal());
        }

        if (top) {
            adjacentNormals.add(triangles.get(i + 1).get(j).normal());
        }

        if (bottom) {
            adjacentNormals.add(triangles.get(i - 1).get(j).normal());
        }

        return adjacentNormals;
    }

    public Vector3D normal(Point3D a, Point3D b, Point3D c) {
        Vector3D vector1 = new Vector3D(a, b);
        Vector3D vector2 = new Vector3D(a, c);
        return vector1.normal(vector2);
    }

    public Vector3D averageVector(Vector3D... vectors) {
        return Stream.of(vectors)
                     .reduce(Vector3D::add)
                     .map(Vector3D::normalize)
                     .orElseThrow(IllegalArgumentException::new);
    }

    public Vector3D averageVector(List<Vector3D> vectors) {
        return vectors.stream()
                      .reduce(Vector3D::add)
                      .map(Vector3D::normalize)
                      .orElseThrow(IllegalArgumentException::new);
    }
}
