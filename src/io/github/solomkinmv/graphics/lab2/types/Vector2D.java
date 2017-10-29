package io.github.solomkinmv.graphics.lab2.types;

public class Vector2D {
    public final double x;
    public final double y;

    public Vector2D(Point2D a, Point2D b) {
        x = b.x - a.x;
        y = b.y - a.y;
    }

    private Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double dotProduct(Vector2D vector2D) {
        return x * vector2D.x + y * vector2D.y;
    }
}
