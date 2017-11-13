package io.github.solomkinmv.graphics.lab2.panels;

import io.github.solomkinmv.graphics.lab2.figures.ZBufferedImage;
import io.github.solomkinmv.graphics.lab2.generator.BezierPolygonsGenerator;
import io.github.solomkinmv.graphics.lab2.types.Point2D;
import io.github.solomkinmv.graphics.lab2.types.Point3D;
import io.github.solomkinmv.graphics.lab2.types.Triangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
public class BezierPanel implements GraphicPanels {
    private static final int SIZE = 1200;
    private FigurePanel figurePanel;
    private JPanel panel;
    private int rotateAngle = 90;
    private int rollAngle = 45;
    private int pitchAngle;
    private int edges = 10;
    private int radius = 30;
    private int height = 30;
    private boolean showNormals;
    private String bezierPoints = "1,1;5,7;8,2;10,10;15,12";
    private List<Point2D> sourcePoints;
    private boolean showGrid;
    private boolean depthColors = true;
    private String cameraPointStr = "1000,1000,1000";
    private Point3D cameraPoint;

    public BezierPanel() {
        parseBezierPoints();
        parseCameraPoint();
        init();
    }

    private void init() {
        panel = new JPanel();
        figurePanel = new FigurePanel();
        panel.add(figurePanel);
        panel.add(createControls());
    }

    private Component createControls() {
        JPanel controlPanel = new JPanel(new GridLayout(11, 2));

        setNavigationButtons(controlPanel);
        setEdgesSlider(controlPanel);
        setRadiusSpinner(controlPanel);
        setHeightSpinner(controlPanel);
        setBezierPointsEditor(controlPanel);
        setCameraPointEditor(controlPanel);
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

    private void setBezierPointsEditor(JPanel controlPanel) {
        JTextField bezierText = new JTextField(bezierPoints);
        bezierText.addActionListener(e -> {
            bezierPoints = bezierText.getText();
            parseBezierPoints();
            repaint();
        });

        controlPanel.add(new Label("Bezier points"));
        controlPanel.add(bezierText);
    }

    private void parseBezierPoints() {
        Function<String, Point2D> stringToPoint = (str) -> {
            String[] xyChunks = str.split(",");
            return new Point2D(Integer.parseInt(xyChunks[0]), Integer.parseInt(xyChunks[1]));
        };
        sourcePoints = Arrays.stream(bezierPoints.split(";"))
                             .map(stringToPoint)
                             .collect(Collectors.toList());
    }


    private void setCameraPointEditor(JPanel controlPanel) {
        JTextField cameraPointText = new JTextField(cameraPointStr);
        cameraPointText.addActionListener(e -> {
            cameraPointStr = cameraPointText.getText();
            parseCameraPoint();
            repaint();
        });

        controlPanel.add(new Label("Camera point: "));
        controlPanel.add(cameraPointText);
    }

    private void parseCameraPoint() {
        String[] cameraPointChunks = cameraPointStr.split(",");
        cameraPoint = new Point3D(Double.parseDouble(cameraPointChunks[0]),
                                  Double.parseDouble(cameraPointChunks[1]),
                                  Double.parseDouble(cameraPointChunks[2]));
    }

    private void repaint() {
        figurePanel.repaint();
    }

    private void setRadiusSpinner(JPanel controlPanel) {
        SpinnerModel spinnerModel = new SpinnerNumberModel(radius, //initial value
                                                           5, //min
                                                           300, //max
                                                           10);//step
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
                                                           5, //min
                                                           300, //max
                                                           10);//step
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

    public JPanel getPanel() {
        return panel;
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

            Triangle[] tris = new BezierPolygonsGenerator(radius, height, edges, edges, sourcePoints).generate();

            Image image = new ZBufferedImage(tris, cameraPoint, SIZE, SIZE, rollAngle, rotateAngle,
                                             pitchAngle, showNormals, showGrid, depthColors).get();

            g2.drawImage(image, 0, 0, null);
        }
    }
}
