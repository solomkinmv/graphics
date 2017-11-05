package io.github.solomkinmv.graphics.lab2;

import io.github.solomkinmv.graphics.lab2.figures.ZBufferedImage;
import io.github.solomkinmv.graphics.lab2.generator.BallPolygonsGenerator;
import io.github.solomkinmv.graphics.lab2.types.Triangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

                Image image = new ZBufferedImage(tris, getHeight(), getWidth(), rotateSlider.getValue(), 0,
                                                 pitchSlider.getValue(), false, false).get();

                g2.drawImage(image, 0, 0, null);
            }
        };
        pane.add(renderPanel, BorderLayout.CENTER);

        rotateSlider.addChangeListener(e -> renderPanel.repaint());
        pitchSlider.addChangeListener(e -> renderPanel.repaint());

        frame.setSize(800, 800);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });
    }
}