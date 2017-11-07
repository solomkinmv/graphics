package io.github.solomkinmv.graphics.lab2;

import io.github.solomkinmv.graphics.lab2.panels.*;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Application {
    public static void main(String[] args) {
        ruBezier();
        portion();
    }

    private static void cylinder() {
        createFrame(new CylinderPanel());
    }

    private static void bezier() {
        createFrame(new BezierPanel());
    }

    private static void portion() {
        createFrame(new PortionPanel());
    }
    private static void ruBezier() {
        createFrame(new RuBezierPanel());
    }

    private static void createFrame(GraphicPanels graphicPanels) {
        Frame frame = new Frame();
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });
        frame.add(graphicPanels.getPanel());
        frame.pack();
        frame.setVisible(true);
    }
}
