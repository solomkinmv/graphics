package io.github.solomkinmv.graphics.lab2.types;

public class Vector3D {
    public final double x;
    public final double y;
    public final double z;

    public Vector3D(Point3D a, Point3D b) {
        x = b.x - a.x;
        y = b.y - a.y;
        z = b.z - a.z;
    }

    private Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D crossProduct(Vector3D otherVector3D) {
        return new Vector3D(y * otherVector3D.z - z * otherVector3D.y,
                            z * otherVector3D.x - x * otherVector3D.z,
                            x * otherVector3D.y - y * otherVector3D.x);
    }

    public Vector3D normal(Vector3D otherVector3D) {
        Vector3D productVector3D = crossProduct(otherVector3D);
        return productVector3D.divide(productVector3D.length());
    }

    public Vector3D divide(double val) {
        return new Vector3D(x / val, y / val, z / val);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }
}
