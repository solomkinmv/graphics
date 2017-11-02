package io.github.solomkinmv.graphics.lab2.generator;

import io.github.solomkinmv.graphics.lab2.types.Point3D;
import io.github.solomkinmv.graphics.lab2.types.Triangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BallPolygonsGenerator extends TetrahedronPolygonsGenerator implements PolygonsGenerator {

    private final int edgeParts;

    public BallPolygonsGenerator(int edgeLength, int edgeParts) {
        super(edgeLength);
        this.edgeParts = edgeParts;
    }

    public BallPolygonsGenerator(int edgeLength) {
        this(edgeLength, 4);
    }

    @Override
    public Triangle[] generate() {
        List<Triangle> triangles = Arrays.asList(super.generate());
        for (int i = 0; i < edgeParts; i++) {
            triangles = inflate(triangles);
        }

        return triangles.toArray(new Triangle[triangles.size()]);
    }

    private List<Triangle> inflate(List<Triangle> tris) {
        List<Triangle> result = new ArrayList<>();
        for (Triangle t : tris) {
            Point3D m1 = new Point3D((t.v1.x + t.v2.x) / 2, (t.v1.y + t.v2.y) / 2, (t.v1.z + t.v2.z) / 2);
            Point3D m2 = new Point3D((t.v2.x + t.v3.x) / 2, (t.v2.y + t.v3.y) / 2, (t.v2.z + t.v3.z) / 2);
            Point3D m3 = new Point3D((t.v1.x + t.v3.x) / 2, (t.v1.y + t.v3.y) / 2, (t.v1.z + t.v3.z) / 2);
            result.add(new Triangle(t.v1, m1, m3, t.color));
            result.add(new Triangle(t.v2, m1, m2, t.color));
            result.add(new Triangle(t.v3, m2, m3, t.color));
            result.add(new Triangle(m1, m2, m3, t.color));
        }
        return result.stream()
                     .map(triangle -> new Triangle(
                             inflate(triangle.v1),
                             inflate(triangle.v2),
                             inflate(triangle.v3),
                             triangle.color
                     )).collect(Collectors.toList());
    }

    private Point3D inflate(Point3D point) {
        double l = Math.sqrt(point.x * point.x + point.y * point.y + point.z * point.z) / Math.sqrt(30000);
        return point.divide(l);
    }
}
