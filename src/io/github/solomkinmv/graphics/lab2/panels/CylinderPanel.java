package io.github.solomkinmv.graphics.lab2.panels;

import io.github.solomkinmv.graphics.lab2.figures.Cylinder;
import io.github.solomkinmv.graphics.lab2.figures.Drawing;
import io.github.solomkinmv.graphics.lab2.graphics.Graphics;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

public class CylinderPanel {
    private static final int SIZE = 1000;
    private int fiAngle;
    private int thetaAngle;
    private GraphicCanvas canvas;
    private JPanel panel;

    public CylinderPanel() {
        canvas = new GraphicCanvas(newCylinderFunciton(), SIZE, SIZE);
        init();
    }

    public JPanel getPanel() {
        return panel;
    }

    private void init() {
        panel = new JPanel();
        panel.add(canvas);
        panel.add(createControls());
    }

    private Component createControls() {
        JPanel controlPanel = new JPanel(new GridLayout(2, 2));

        JButton leftButton = new JButton("<");
        leftButton.addActionListener(e -> {
            fiAngle += 20;
            canvas.setDrawingFunction(newCylinderFunciton());
            canvas.repaint();
        });

        JButton rightButton = new JButton(">");
        rightButton.addActionListener(e -> {
            fiAngle -= 20;
            canvas.setDrawingFunction(newCylinderFunciton());
            canvas.repaint();
        });

        JButton upButton = new JButton("up");
        upButton.addActionListener(e -> {
            thetaAngle += 20;
            canvas.setDrawingFunction(newCylinderFunciton());
            canvas.repaint();
        });

        JButton downButton = new JButton("down");
        downButton.addActionListener(e -> {
            thetaAngle -= 20;
            canvas.setDrawingFunction(newCylinderFunciton());
            canvas.repaint();
        });

        controlPanel.add(leftButton);
        controlPanel.add(rightButton);
        controlPanel.add(upButton);
        controlPanel.add(downButton);
        return controlPanel;
    }

    private Function<Graphics, Drawing> newCylinderFunciton() {
        return graphics -> new Cylinder(graphics, fiAngle, thetaAngle);
    }

}
