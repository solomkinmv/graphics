package io.github.solomkinmv.graphics.lab2.types;

public class Triangle {
    public final Point3D v1;
    public final Point3D v2;
    public final Point3D v3;

    public Triangle(Point3D v1, Point3D v2, Point3D v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public Vector3D normal() {
        Vector3D vector1 = new Vector3D(v1, v2);
        Vector3D vector2 = new Vector3D(v3, v2);
        return vector1.crossProduct(vector2);
    }
}
