package io.github.solomkinmv.graphics.lab2.panels;

import io.github.solomkinmv.graphics.lab2.figures.ZBufferedImage;
import io.github.solomkinmv.graphics.lab2.generator.CylinderPolygonsGenerator;
import io.github.solomkinmv.graphics.lab2.types.Triangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

public class CylinderPanel implements GraphicPanels {
    private static final int SIZE = 1000;
    private FigurePanel figurePanel;
    private int rotateAngle = 90;
    private int rollAngle = 45;
    private int pitchAngle;
    private JPanel panel;
    private int edges = 10;
    private int radius = 200;
    private int height = 300;
    private boolean showNormals;
    private boolean showGrid;
    private boolean depthColors = true;

    public CylinderPanel() {
        init();
    }

    public JPanel getPanel() {
        return panel;
    }

    private void init() {
        panel = new JPanel();
        figurePanel = new FigurePanel();
        panel.add(figurePanel);
        panel.add(createControls());
    }

    private Component createControls() {
        JPanel controlPanel = new JPanel(new GridLayout(9, 2));

        setNavigationButtons(controlPanel);
        setEdgesSlider(controlPanel);
        setRadiusSpinner(controlPanel);
        setHeightSpinner(controlPanel);
        setCheckBoxes(controlPanel);

        return controlPanel;
    }

    private void setCheckBoxes(JPanel controlPanel) {
        JCheckBox normalsCheckBox = new JCheckBox("Show normals", showNormals);
        normalsCheckBox.addItemListener(e -> {
            showNormals = e.getStateChange() == ItemEvent.SELECTED;
            repaint();
        });
        controlPanel.add(normalsCheckBox);

        JCheckBox gridCheckBox = new JCheckBox("Show grid", showGrid);
        gridCheckBox.addItemListener(e -> {
            showGrid = e.getStateChange() == ItemEvent.SELECTED;
            repaint();
        });
        controlPanel.add(gridCheckBox);

        JCheckBox depthColorsCheckBox = new JCheckBox("Use depth colors", depthColors);
        depthColorsCheckBox.addItemListener(e -> {
            depthColors = e.getStateChange() == ItemEvent.SELECTED;
            repaint();
        });
        controlPanel.add(depthColorsCheckBox);
    }

    private void setRadiusSpinner(JPanel controlPanel) {
        SpinnerModel spinnerModel = new SpinnerNumberModel(radius, //initial value
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
        SpinnerModel spinnerModel = new SpinnerNumberModel(height, //initial value
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
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 3, 30, edges);

        slider.setValue(edges);
        slider.addChangeListener(e -> {
            edges = ((JSlider) e.getSource()).getValue();
            repaint();
        });
        controlPanel.add(new Label("edges"));
        controlPanel.add(slider);
    }

    private void repaint() {
        figurePanel.repaint();
    }

    private void setNavigationButtons(JPanel controlPanel) {
        JButton leftButton = new JButton("left");
        leftButton.addActionListener(e -> {
            rotateAngle = (rotateAngle + 10) % 360;
            repaint();
        });

        JButton rightButton = new JButton("right");
        rightButton.addActionListener(e -> {
            rotateAngle = (rotateAngle - 10) % 360;
            repaint();
        });

        JButton pitchLeftButton = new JButton("pitch left");
        pitchLeftButton.addActionListener(e -> {
            pitchAngle = (pitchAngle + 10) % 360;
            repaint();
        });

        JButton pitchRightButton = new JButton("pitch right");
        pitchRightButton.addActionListener(e -> {
            pitchAngle = (pitchAngle - 10) % 360;
            repaint();
        });

        JButton upButton = new JButton("up");
        upButton.addActionListener(e -> {
            rollAngle = (rollAngle + 10) % 360;
            repaint();
        });

        JButton downButton = new JButton("down");
        downButton.addActionListener(e -> {
            rollAngle = (rollAngle - 10) % 360;
            repaint();
        });

        controlPanel.add(leftButton);
        controlPanel.add(rightButton);
        controlPanel.add(pitchLeftButton);
        controlPanel.add(pitchRightButton);
        controlPanel.add(upButton);
        controlPanel.add(downButton);
    }

    class FigurePanel extends JPanel {

        private FigurePanel() {
            setPreferredSize(new Dimension(SIZE, SIZE));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.white);
            g2.fillRect(0, 0, SIZE, SIZE);

            Triangle[] tris = new CylinderPolygonsGenerator(radius, height, edges, edges).generate();

            Image image = new ZBufferedImage(tris, SIZE, SIZE, rollAngle, rotateAngle,
                                             pitchAngle, showNormals, showGrid, depthColors).get();

            g2.drawImage(image, 0, 0, null);
        }
    }
}
