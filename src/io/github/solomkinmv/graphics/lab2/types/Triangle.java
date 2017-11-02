package io.github.solomkinmv.graphics.lab2.types;

import java.awt.*;

public class Triangle {
    public final Point3D v1;
    public final Point3D v2;
    public final Point3D v3;
    public final Color color;

    public Triangle(Point3D v1, Point3D v2, Point3D v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        color = Color.white;
    }

    public Triangle(Point3D v1, Point3D v2, Point3D v3, Color color) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.color = color;
    }

    public Vector3D normal() {
        Vector3D vector1 = new Vector3D(v1, v2);
        Vector3D vector2 = new Vector3D(v1, v3);
        return vector1.normal(vector2);
    }

    public Point3D center() {
        Point3D m = new Point3D((v2.x + v3.x) / 2, (v2.y + v3.y) / 2, (v2.z + v3.z) / 2);
        return new Point3D((v1.x + 2 * m.x) / 3, (v1.y + 2 * m.y) / 3, (v1.z + 2 * m.z) / 3);
    }

    @Override
    public String toString() {
        return String.format("Vector[(%.2f, %.2f, %.2f), (%.2f, %.2f, %.2f), (%.2f, %.2f, %.2f)]", v1.x, v1.y, v1.z, v2.x, v2.y, v2.z, v3.x, v3.y, v3.z);
    }
}
