package com.preibisch.bdvtransform.panels.utils;

import net.imglib2.realtransform.AffineTransform3D;

import javax.swing.*;
import java.awt.*;

public class transformationsPanel extends JPanel {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test");
        frame.setLayout(new GridLayout(0, 1));
        frame.setSize(new Dimension(200, 600));
        JPanel all = new JPanel(new GridLayout(0, 1));
        JScrollPane scrollFrame = new JScrollPane(all);
        all.setAutoscrolls(true);
        frame.add(scrollFrame);
        all.add(new TransformationView(new AffineTransform3D()));
        all.add(new TransformationView(new AffineTransform3D()));
        all.add(new TransformationView(new AffineTransform3D()));
        all.add(new TransformationView(new AffineTransform3D()));
        all.add(new TransformationView(new AffineTransform3D()));
        all.add(new TransformationView(new AffineTransform3D()));
        all.add(new TransformationView(new AffineTransform3D()));
        frame.setVisible(true);
    }
}
