package com.preibisch.bdvtransform.panels.utils.tansformation;

import com.preibisch.bdvtransform.panels.utils.AdaptiveLabel;
import net.imglib2.realtransform.AffineTransform3D;

import javax.swing.*;
import java.awt.*;

public class TransformationView extends JPanel {
    private final static int MAX_SIZE = 170;

    public TransformationView(AffineTransform3D transform) {
        super();

        JPanel mainPane = new JPanel(new FlowLayout());
        mainPane.setPreferredSize(new Dimension(MAX_SIZE,MAX_SIZE-20));
        JPanel matrixPane = new JPanel(new GridLayout(3, 4));
        JButton button = new JButton("-");
        button.setPreferredSize(new Dimension(20,20));
        matrixPane.setPreferredSize(new Dimension(MAX_SIZE-30, MAX_SIZE-30));
        for (double x : transform.getRowPackedCopy()) {
            AdaptiveLabel label = new AdaptiveLabel(String.valueOf(x));
            matrixPane.add(label);
        }
        mainPane.setBackground(Color.PINK);
        matrixPane.setBackground(Color.WHITE);
        mainPane.add(button);
        mainPane.add(matrixPane);
        this.add(mainPane);
    }

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
