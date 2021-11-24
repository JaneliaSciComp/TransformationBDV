package com.preibisch.bdvtransform.panels;

import com.preibisch.bdvtransform.panels.utils.tansformation.TransformationView;
import net.imglib2.realtransform.AffineTransform3D;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TransformationsPanel extends JPanel {
    public TransformationsPanel(List<AffineTransform3D> transformations) {
        super(new GridLayout(0, 1));
        setBackground(Color.BLACK);

        for (AffineTransform3D transformation :
                transformations) {
            add(new TransformationView(transformation));
        }

        setAutoscrolls(true);
    }

    public static void main(String[] args) {

        List<AffineTransform3D> transformations = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            transformations.add(new AffineTransform3D());

        TransformationsPanel transformationsPanel = new TransformationsPanel(transformations);

        JFrame frame = new JFrame("Test");
        frame.setLayout(new GridLayout(0, 1));
        frame.setSize(new Dimension(200, 600));
        JScrollPane scrollFrame = new JScrollPane(transformationsPanel);
        frame.add(scrollFrame);
        frame.setVisible(true);
    }
}
