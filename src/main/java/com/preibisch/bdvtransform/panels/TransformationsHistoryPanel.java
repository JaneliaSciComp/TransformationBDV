package com.preibisch.bdvtransform.panels;

import com.preibisch.bdvtransform.panels.utils.tansformation.ImageTransformation;
import com.preibisch.bdvtransform.panels.utils.tansformation.ImageTransformations;
import com.preibisch.bdvtransform.panels.utils.tansformation.TransformationPanelUpdater;
import com.preibisch.bdvtransform.panels.utils.tansformation.TransformationView;
import net.imglib2.realtransform.AffineTransform3D;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class TransformationsHistoryPanel extends BDVCardPanel {
    private final TransformationPanelUpdater updater;
    private JPanel mainPanel;

    public TransformationsHistoryPanel(ImageTransformations transformations, TransformationPanelUpdater transformationPanelUpdater) {
        super("History", "History", new GridLayout(0, 1), true);
        this.updater = transformationPanelUpdater;

        setSize(new Dimension(180, 600));
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0, 1));

        JScrollPane scrollFrame = new JScrollPane(mainPanel);
        add(scrollFrame);

        mainPanel.setAutoscrolls(true);

        setAutoscrolls(true);
        addAll(transformations);
    }

    private void addAll(ImageTransformations transformations) {
        int max = transformations.getCurrentPosition();
        int i = 0;
        for (Integer key : transformations.getAll().keySet()) {
            if(i>max) break;
            addTransformation(key, transformations.getAll().get(key));
            i++;
        }
    }


    public void addTransformation(Integer i, ImageTransformation imageTransformation) {
        mainPanel.add(new TransformationView(i, imageTransformation, (position, panel) -> {
            if (updater.remove(position)) {
                mainPanel.remove(panel);
                updateView();
            }
        }));
        updateView();
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

    public void addTransformation(Map.Entry<Integer, ImageTransformation> entry) {
        addTransformation(entry.getKey(), entry.getValue());
    }

    public static void main(String[] args) {
        ImageTransformations transformations = new ImageTransformations();
        for (int i = 0; i < 10; i++)
            transformations.add(new AffineTransform3D());
        TransformationsHistoryPanel transformationsPanel = new TransformationsHistoryPanel(transformations, position -> {
            System.out.println(position);
            return true;
        });
        JFrame frame = new JFrame("Test");
        frame.setLayout(new GridLayout(0, 1));
        frame.setSize(new Dimension(200, 600));
        JScrollPane scrollFrame = new JScrollPane(transformationsPanel);
        frame.add(scrollFrame);
        frame.setVisible(true);
    }

    public void refresh() {
        updateView();
    }
}
