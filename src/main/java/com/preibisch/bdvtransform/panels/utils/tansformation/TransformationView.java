package com.preibisch.bdvtransform.panels.utils.tansformation;

import com.preibisch.bdvtransform.panels.utils.AdaptiveLabel;
import net.imglib2.realtransform.AffineTransform3D;

import javax.swing.*;
import java.awt.*;

public class TransformationView extends JPanel {
    private final static int MAX_SIZE = 180;
    private final ImageTransformation imageTransformation;

    public TransformationView(Integer position, ImageTransformation imageTransformation, TransformationViewUpdater updater) {
        super();
        this.imageTransformation = imageTransformation;

        JPanel mainPane = new JPanel(new FlowLayout());
        mainPane.setPreferredSize(new Dimension(MAX_SIZE, MAX_SIZE - 20));
        JPanel matrixPane = new JPanel(new GridLayout(3, 4));
        JButton button = new JButton("-");
        button.addActionListener(e -> updater.remove(position, this));
        button.setPreferredSize(new Dimension(20, 20));
        matrixPane.setPreferredSize(new Dimension(MAX_SIZE - 30, MAX_SIZE - 30));
        for (double x : imageTransformation.getTransformation().getRowPackedCopy()) {
            AdaptiveLabel label = new AdaptiveLabel(String.valueOf(x));
            matrixPane.add(label);
        }
        mainPane.setBackground(Color.PINK);
        mainPane.add(button);
        mainPane.add(matrixPane);
        this.add(mainPane);
    }

    public interface TransformationViewUpdater {
        void remove(int position, JPanel panel);
    }


    public TransformationView(AffineTransform3D transform3D) {
        this(0, transform3D, null);
    }

    public TransformationView(int position, AffineTransform3D transform, TransformationViewUpdater updater) {
        this(position, new ImageTransformation(transform), updater);

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
