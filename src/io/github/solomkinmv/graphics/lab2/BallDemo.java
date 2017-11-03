package io.github.solomkinmv.graphics.lab2;

import io.github.solomkinmv.graphics.lab2.generator.BallPolygonsGenerator;
import io.github.solomkinmv.graphics.lab2.graphics.Transformer;
import io.github.solomkinmv.graphics.lab2.types.Point3D;
import io.github.solomkinmv.graphics.lab2.types.Triangle;
import io.github.solomkinmv.graphics.lab2.types.Vector3D;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BallDemo {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());

        // slider to control horizontal rotation
        JSlider rotateSlider = new JSlider(-180, 180, 0);
        pane.add(rotateSlider, BorderLayout.SOUTH);

        // slider to control vertical rotation
        JSlider pitchSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
        pane.add(pitchSlider, BorderLayout.EAST);

        // panel to display render results
        JPanel renderPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());

                Triangle[] tris = new BallPolygonsGenerator(100).generate();

                Transformer transform = new Transformer(rotateSlider.getValue(), 0, pitchSlider.getValue());

                BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

                double[] zBuffer = new double[img.getWidth() * img.getHeight()];
                // initialize array with extremely far away depths
                for (int q = 0; q < zBuffer.length; q++) {
                    zBuffer[q] = Double.NEGATIVE_INFINITY;
                }
                for (Triangle t : tris) {
                    t = transform.transform(t);
                    t = t.add(getWidth() / 2., getHeight() / 2., 0);

                    Vector3D norm = t.normal();

                    double angleCos = Math.abs(norm.z);


                    double triangleArea = t.area();

                    for (int y = t.minY(); y <= t.maxY(); y++) {
                        for (int x = t.minX(); x <= t.maxX(); x++) {
                            double b1 = ((y - t.v3.y) * (t.v2.x - t.v3.x) + (t.v2.y - t.v3.y) * (t.v3.x - x)) / triangleArea;
                            double b2 = ((y - t.v1.y) * (t.v3.x - t.v1.x) + (t.v3.y - t.v1.y) * (t.v1.x - x)) / triangleArea;
                            double b3 = ((y - t.v2.y) * (t.v1.x - t.v2.x) + (t.v1.y - t.v2.y) * (t.v2.x - x)) / triangleArea;
                            if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                                double depth = b1 * t.v1.z + b2 * t.v2.z + b3 * t.v3.z;
                                int zIndex = y * img.getWidth() + x;
                                if (zBuffer[zIndex] < depth) {
                                    img.setRGB(x, y, getShade(t.color, angleCos).getRGB());
                                    zBuffer[zIndex] = depth;
                                }
                            }
                        }
                    }

                }

                g2.drawImage(img, 0, 0, null);
            }
        };
        pane.add(renderPanel, BorderLayout.CENTER);

        rotateSlider.addChangeListener(e -> renderPanel.repaint());
        pitchSlider.addChangeListener(e -> renderPanel.repaint());

        frame.setSize(800, 800);
        frame.setVisible(true);
    }

    private static Color getShade(Color color, double shade) {
        double redLinear = Math.pow(color.getRed(), 2.4) * shade;
        double greenLinear = Math.pow(color.getGreen(), 2.4) * shade;
        double blueLinear = Math.pow(color.getBlue(), 2.4) * shade;

        int red = (int) Math.pow(redLinear, 1 / 2.4);
        int green = (int) Math.pow(greenLinear, 1 / 2.4);
        int blue = (int) Math.pow(blueLinear, 1 / 2.4);

        return new Color(red, green, blue);
    }
}