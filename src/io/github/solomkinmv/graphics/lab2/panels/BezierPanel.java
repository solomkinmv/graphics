package io.github.solomkinmv.graphics.lab2.panels;

import io.github.solomkinmv.graphics.lab2.figures.Bezier;
import io.github.solomkinmv.graphics.lab2.figures.BezierFlat;
import io.github.solomkinmv.graphics.lab2.figures.Drawing;
import io.github.solomkinmv.graphics.lab2.graphics.Graphics;
import io.github.solomkinmv.graphics.lab2.points.Point2D;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class BezierPanel implements GraphicPanels {
    private static final int SIZE = 1200;
    private final GraphicCanvas flatCanvas;
    private JPanel panel;
    private GraphicCanvas canvas;
    private int fiAngle = 220;
    private int thetaAngle = 60;
    private int edges = 10;
    private int radius = 30;
    private int height = 30;
    private List<Point2D> sourcePoints = Arrays.asList(new Point2D(1, 1),
                                                       new Point2D(5, 7),
                                                       new Point2D(8, 2));

    public BezierPanel() {
        canvas = new GraphicCanvas(newBezierFunction(), SIZE, SIZE);
        flatCanvas = new GraphicCanvas(newFlatBezierFunction(), SIZE / 2, SIZE / 2);
        init();
    }

    private void init() {
        panel = new JPanel(new BorderLayout());
        panel.add(canvas, BorderLayout.CENTER);

        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.add(createFlatPanel(), BorderLayout.NORTH);
        sidePanel.add(createControls(), BorderLayout.SOUTH);
        panel.add(sidePanel, BorderLayout.EAST);
    }

    private Component createFlatPanel() {
        JPanel panel = new JPanel();
        panel.add(flatCanvas);
        return panel;
    }

    private Component createControls() {
        JPanel controlPanel = new JPanel(new GridLayout(5, 2));

        setNavigationButtons(controlPanel);
        setEdgesSlider(controlPanel);
        setRadiusSpinner(controlPanel);
        setHeightSpinner(controlPanel);

        return controlPanel;
    }

    private void repaint() {
        canvas.setDrawingFunction(newBezierFunction());
        canvas.repaint();

        flatCanvas.setDrawingFunction(newFlatBezierFunction());
        flatCanvas.repaint();
    }

    private void setRadiusSpinner(JPanel controlPanel) {
        SpinnerModel spinnerModel = new SpinnerNumberModel(200, //initial value
                                                           20, //min
                                                           300, //max
                                                           20);//step
        JSpinner spinner = new JSpinner(spinnerModel);
        spinner.addChangeListener(e -> {
            radius = ((Number) ((JSpinner) e.getSource()).getValue()).intValue();
            repaint();
        });
        controlPanel.add(new Label("Radius"));
        controlPanel.add(spinner);
    }

    private void setHeightSpinner(JPanel controlPanel) {
        SpinnerModel spinnerModel = new SpinnerNumberModel(200, //initial value
                                                           20, //min
                                                           300, //max
                                                           20);//step
        JSpinner spinner = new JSpinner(spinnerModel);
        spinner.addChangeListener(e -> {
            height = ((Number) ((JSpinner) e.getSource()).getValue()).intValue();
            repaint();
        });
        controlPanel.add(new Label("Height"));
        controlPanel.add(spinner);
    }

    private void setEdgesSlider(JPanel controlPanel) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 2, 50, edges);

        slider.setValue(edges);
        slider.addChangeListener(e -> {
            edges = ((JSlider) e.getSource()).getValue();
            repaint();
        });
        controlPanel.add(new Label("edges"));
        controlPanel.add(slider);
    }

    private void setNavigationButtons(JPanel controlPanel) {
        JButton leftButton = new JButton("left");
        leftButton.addActionListener(e -> {
            fiAngle += 10;
            repaint();
        });

        JButton rightButton = new JButton("right");
        rightButton.addActionListener(e -> {
            fiAngle -= 10;
            repaint();
        });

        JButton upButton = new JButton("up");
        upButton.addActionListener(e -> {
            thetaAngle -= 10;
            repaint();
        });

        JButton downButton = new JButton("down");
        downButton.addActionListener(e -> {
            thetaAngle += 10;
            repaint();
        });

        controlPanel.add(leftButton);
        controlPanel.add(rightButton);
        controlPanel.add(upButton);
        controlPanel.add(downButton);
    }

    public JPanel getPanel() {
        return panel;
    }

    private Function<Graphics, Drawing> newBezierFunction() {
        return graphics -> new Bezier(sourcePoints, graphics, fiAngle, thetaAngle, edges, edges, radius, height);
    }

    private Function<Graphics, Drawing> newFlatBezierFunction() {
        return graphics -> new BezierFlat(sourcePoints, graphics, edges, radius, height);
    }
}
