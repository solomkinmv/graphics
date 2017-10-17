package io.github.solomkinmv.graphics.lab2;

import io.github.solomkinmv.graphics.lab2.panels.CylinderPanel;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Application {
    public static void main(String[] args) {
        cylinder();
        cylinder();
    }

    private static void cylinder() {
        Frame frame = new Frame();
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });
        frame.add(new CylinderPanel().getPanel());
        frame.pack();
        frame.setVisible(true);
    }
}
