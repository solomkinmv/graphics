package io.github.solomkinmv.graphics.lab2.types;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Triangle2D {
    public final Point2D v1;
    public final Point2D v2;
    public final Point2D v3;

    public Triangle2D(Point2D v1, Point2D v2, Point2D v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }


    public int minX() {
        return minByAxis(Point2D::getX);
    }

    public int minY() {
        return minByAxis(Point2D::getY);
    }

    public int maxX() {
        return maxByAxis(Point2D::getX);
    }

    public int maxY() {
        return maxByAxis(Point2D::getY);
    }

    private int minByAxis(Function<Point2D, Integer> toAxisValue) {
        return minMaxByAxis(Math::min, toAxisValue);
    }

    private int maxByAxis(Function<Point2D, Integer> toAxisValue) {
        return minMaxByAxis(Math::max, toAxisValue);
    }

    private int minMaxByAxis(BiFunction<Integer, Integer, Integer> minMax, Function<Point2D, Integer> toAxisValue) {
        return minMax.apply(toAxisValue.apply(v1), minMax.apply(toAxisValue.apply(v2), toAxisValue.apply(v3)));
    }
}
