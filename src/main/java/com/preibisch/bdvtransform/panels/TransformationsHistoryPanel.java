package com.preibisch.bdvtransform.panels;

import com.preibisch.bdvtransform.panels.utils.tansformation.ImageTransformation;
import com.preibisch.bdvtransform.panels.utils.tansformation.ImageTransformations;
import com.preibisch.bdvtransform.panels.utils.tansformation.TransformationPanelUpdater;
import com.preibisch.bdvtransform.panels.utils.tansformation.TransformationView;
import net.imglib2.realtransform.AffineTransform3D;

import javax.swing.*;
import java.awt.*;

public class TransformationsHistoryPanel extends BDVCardPanel {
    private final TransformationPanelUpdater updater;
    private JPanel mainPanel;

    public TransformationsHistoryPanel(ImageTransformations transformations, TransformationPanelUpdater transformationPanelUpdater) {
        super("History", "History", new GridLayout(0, 1),true);
        this.updater = transformationPanelUpdater;

//        setSize(new Dimension(180, 600));
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0, 1));

        JScrollPane scrollFrame = new JScrollPane(mainPanel);
        add(scrollFrame);

        mainPanel.setAutoscrolls(true);

        setAutoscrolls(true);
        addAll(transformations);
    }

    private void addAll(ImageTransformations transformations) {
        for (Integer i : transformations.getAll().keySet()) {
            addPanel(i, transformations.getAll().get(i));
        }
    }

    private void addPanel(Integer i, ImageTransformation imageTransformation) {
        mainPanel.add(new TransformationView(i, imageTransformation, (position, panel) -> {
            updater.remove(position);
            TransformationsHistoryPanel.this.remove(panel);
            updateView();
        }));
    }

    @Override
    protected void updateView() {
        revalidate();
        repaint();
    }

    public void setAll(ImageTransformations transformations) {
        mainPanel.removeAll();
        addAll(transformations);
    }


    public static void main(String[] args) {

        ImageTransformations transformations = new ImageTransformations();
        for (int i = 0; i < 10; i++)
            transformations.add(new AffineTransform3D());

        TransformationsHistoryPanel transformationsPanel = new TransformationsHistoryPanel(transformations, new TransformationPanelUpdater() {
            @Override
            public void remove(int position) {
                System.out.println(position);
            }
        });

        JFrame frame = new JFrame("Test");
        frame.setLayout(new GridLayout(0, 1));
        frame.setSize(new Dimension(200, 600));
        JScrollPane scrollFrame = new JScrollPane(transformationsPanel);
        frame.add(scrollFrame);
        frame.setVisible(true);
    }
}
