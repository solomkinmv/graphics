package io.github.solomkinmv.graphics.lab2.types;

import java.awt.*;
import java.util.Vector;

public class NormalizedTriangle extends Triangle {

    public final Vector3D n1;
    public final Vector3D n2;
    public final Vector3D n3;

    public NormalizedTriangle(Point3D v1, Point3D v2, Point3D v3, Vector3D n1, Vector3D n2, Vector3D n3) {
        super(v1, v2, v3);
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;
    }

    public NormalizedTriangle(Point3D v1, Point3D v2, Point3D v3, Color color, Vector3D n1, Vector3D n2, Vector3D n3) {
        super(v1, v2, v3, color);
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;
    }
}
