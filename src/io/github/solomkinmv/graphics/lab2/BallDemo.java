package io.github.solomkinmv.graphics.lab2;

import io.github.solomkinmv.graphics.lab2.generator.BallPolygonsGenerator;
import io.github.solomkinmv.graphics.lab2.graphics.Transformer;
import io.github.solomkinmv.graphics.lab2.types.Triangle;

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

                    for (int y = t.minY(); y <= t.maxY(); y++) {
                        for (int x = t.minX(); x <= t.maxX(); x++) {
                            if (t.containsPoint(x, y)) {
                                double depth = t.depth(x, y);
                                int zIndex = y * img.getWidth() + x;
                                if (zBuffer[zIndex] < depth) {
                                    img.setRGB(x, y, t.shade().getRGB());
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
}