package io.github.solomkinmv.graphics.lab2.panels;

import io.github.solomkinmv.graphics.lab2.figures.Bezier;
import io.github.solomkinmv.graphics.lab2.figures.Drawing;
import io.github.solomkinmv.graphics.lab2.graphics.Graphics;
import io.github.solomkinmv.graphics.lab2.points.Point2D;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class BezierPanel implements GraphicPanels {
    private static final int SIZE = 1000;
    private JPanel panel;
    private GraphicCanvas canvas;
    private List<Point2D> sourcePoints = Arrays.asList(new Point2D(1, 1),
                                                       new Point2D(5, 5),
                                                       new Point2D(10, 1));

    public BezierPanel() {
        canvas = new GraphicCanvas(newBezierFunction(), SIZE, SIZE);
        init();
    }

    private void init() {
        panel = new JPanel();
        panel.add(canvas);
    }

    public JPanel getPanel() {
        return panel;
    }

    private Function<Graphics, Drawing> newBezierFunction() {
        return graphics -> new Bezier(sourcePoints, graphics, 220, 60, 10, 10, 10, 8);
    }
}
