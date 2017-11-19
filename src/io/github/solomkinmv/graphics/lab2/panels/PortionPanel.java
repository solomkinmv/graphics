package io.github.solomkinmv.graphics.lab2.panels;

import io.github.solomkinmv.graphics.lab2.figures.ZBufferedImage;
import io.github.solomkinmv.graphics.lab2.generator.PortionPolygonsGenerator;
import io.github.solomkinmv.graphics.lab2.types.Point3D;
import io.github.solomkinmv.graphics.lab2.types.Triangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.function.Function;

public class PortionPanel implements GraphicPanels {
    private static final int SIZE = 1000;
    private int rotateAngle = 90;
    private int rollAngle = 45;
    private int pitchAngle;
    private JPanel panel;
    private int edges = 10;
    private String line1Str = "0,0,0;0,0.5,1;0,1,0";
    private String line2Str = "1,0,0;1,0.5,0;1,1,0";
    private Point3D[] line1 = parseBezierPoints(line1Str);
    private Point3D[] line2 = parseBezierPoints(line2Str);
    private boolean showNormals;
    private boolean showGrid = false;
    private FigurePanel figurePanel;

    public PortionPanel() {
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
        JPanel controlPanel = new JPanel(new GridLayout(8, 2));

        setNavigationButtons(controlPanel);
        setEdgesSlider(controlPanel);
        setFi1Text(controlPanel);
        setFi2Text(controlPanel);
        setNormalsShowCheckBox(controlPanel);

        return controlPanel;
    }

    private void setNormalsShowCheckBox(JPanel controlPanel) {
        JCheckBox normalsCheckBox = new JCheckBox("Показать нормали");
        normalsCheckBox.addItemListener(e -> {
            showNormals = e.getStateChange() == ItemEvent.SELECTED;
            repaint();
        });
        controlPanel.add(normalsCheckBox);
    }

    private void setFi1Text(JPanel controlPanel) {
        JTextField bezierText = new JTextField(line1Str);
        bezierText.addActionListener(e -> {
            line1Str = bezierText.getText();
            line1 = parseBezierPoints(line1Str);
            repaint();
        });

        controlPanel.add(new Label("Fi 1"));
        controlPanel.add(bezierText);
    }

    private void setFi2Text(JPanel controlPanel) {
        JTextField bezierText = new JTextField(line2Str);
        bezierText.addActionListener(e -> {
            line2Str = bezierText.getText();
            line2 = parseBezierPoints(line2Str);
            repaint();
        });

        controlPanel.add(new Label("Fi 2"));
        controlPanel.add(bezierText);
    }

    private Point3D[] parseBezierPoints(String line) {
        Function<String, Point3D> stringToPoint = (str) -> {
            String[] xyChunks = str.split(",");
            return new Point3D(Float.parseFloat(xyChunks[0]), Float.parseFloat(xyChunks[1]),
                               Float.parseFloat(xyChunks[2]));
        };
        return Arrays.stream(line.split(";"))
                     .map(stringToPoint)
                     .toArray(Point3D[]::new);
    }

    private void setEdgesSlider(JPanel controlPanel) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 2, 50, edges);

        slider.setValue(edges);
        slider.addChangeListener(e -> {
            edges = ((JSlider) e.getSource()).getValue();
            repaint();
        });
        controlPanel.add(new Label("Сетка"));
        controlPanel.add(slider);
    }

    private void repaint() {
        figurePanel.repaint();
    }

    private void setNavigationButtons(JPanel controlPanel) {
        JButton leftButton = new JButton("Повернуть влево");
        leftButton.addActionListener(e -> {
            rotateAngle = (rotateAngle + 10) % 360;
            repaint();
        });

        JButton rightButton = new JButton("Повернуть вправо");
        rightButton.addActionListener(e -> {
            rotateAngle = (rotateAngle - 10) % 360;
            repaint();
        });

        JButton pitchLeftButton = new JButton("Наклонить влево");
        pitchLeftButton.addActionListener(e -> {
            pitchAngle = (pitchAngle + 10) % 360;
            repaint();
        });

        JButton pitchRightButton = new JButton("Наклонить вправо");
        pitchRightButton.addActionListener(e -> {
            pitchAngle = (pitchAngle - 10) % 360;
            repaint();
        });

        JButton upButton = new JButton("Повернуть вверх");
        upButton.addActionListener(e -> {
            rollAngle = (rollAngle - 10) % 360;
            repaint();
        });

        JButton downButton = new JButton("Повернуть вниз");
        downButton.addActionListener(e -> {
            rollAngle = (rollAngle + 10) % 360;
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
        protected void paintComponent(java.awt.Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.white);
            g2.fillRect(0, 0, SIZE, SIZE);

            Triangle[] tris = new PortionPolygonsGenerator(line1, line2, edges, edges).generate();

            Image image = new ZBufferedImage(tris, SIZE, SIZE, rollAngle, rotateAngle,
                                             pitchAngle, showNormals, showGrid, true).get();

            g2.drawImage(image, 0, 0, null);
        }
    }

}
