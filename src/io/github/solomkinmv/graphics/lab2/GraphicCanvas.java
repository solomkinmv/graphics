package io.github.solomkinmv.graphics.lab2;

import io.github.solomkinmv.graphics.lab2.figures.Drawing;
import io.github.solomkinmv.graphics.lab2.graphics.CenteredGraphics;
import io.github.solomkinmv.graphics.lab2.graphics.DefaultGraphics;
import io.github.solomkinmv.graphics.lab2.graphics.Graphics;

import java.awt.*;
import java.util.function.Function;

public class GraphicCanvas extends Canvas {
    private final Function<Graphics, Drawing> drawingFunction;
    private final int height;
    private final int width;

    public GraphicCanvas(Function<Graphics, Drawing> drawingFunction, int height, int width) {
        this.drawingFunction = drawingFunction;
        this.height = height;
        this.width = width;
    }

    public void paint(java.awt.Graphics g) {
        setBackground(Color.white);
        g.setColor(Color.black);

        drawingFunction.apply(
                new CenteredGraphics(new DefaultGraphics(g),
                                     height,
                                     width))
                       .draw();
    }
}