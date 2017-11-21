package io.github.julia.graphics.lab2.types;

public class Vector3D {
    public final double x;
    public final double y;
    public final double z;

    public Vector3D(Point3D a, Point3D b) {
        x = b.x - a.x;
        y = b.y - a.y;
        z = b.z - a.z;
    }

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D add(Vector3D otherVector3D) {
        return new Vector3D(x + otherVector3D.x, y + otherVector3D.y, z + otherVector3D.z);
    }

    public Vector3D crossProduct(Vector3D otherVector3D) {
        return new Vector3D(y * otherVector3D.z - z * otherVector3D.y,
                            z * otherVector3D.x - x * otherVector3D.z,
                            x * otherVector3D.y - y * otherVector3D.x);
    }

    public double dotProduct(Vector3D otherVector3D) {
        return x * otherVector3D.x + y * otherVector3D.y + z * otherVector3D.z;
    }

    public Vector3D normal(Vector3D otherVector3D) {
        return crossProduct(otherVector3D).normalize();
    }

    public Vector3D divide(double val) {
        return new Vector3D(x / val, y / val, z / val);
    }

    public Vector3D mul(double val) {
        return new Vector3D(x * val, y * val, z * val);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3D normalize() {
        return divide(length());
    }

    public double angle(Vector3D vector) {
        double v = x * vector.x + y * vector.y + z * vector.z;
        return Math.toDegrees(Math.acos(v / length() / vector.length()));
    }
}
