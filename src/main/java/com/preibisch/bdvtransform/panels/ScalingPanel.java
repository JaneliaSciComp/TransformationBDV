package com.preibisch.bdvtransform.panels;

import com.preibisch.bdvtransform.panels.utils.TransformationUpdater;
import net.imglib2.realtransform.AffineTransform3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScalingPanel extends BDVCardPanel implements ActionListener {
    private final TransformationUpdater updater;
    private AffineTransform3D transform;
    private JTextField scale;


    public ScalingPanel(AffineTransform3D transform, TransformationUpdater updater) {
        super("ScalePanel","Scale",new GridLayout(0, 1));
        this.transform = transform;
        this.updater = updater;
        this.scale = new JTextField(String.valueOf(0.0));
        this.add(getPanel("Scale: ", this.scale));
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        this.add(updateButton);
    }

    private JPanel getPanel(String s, JTextField field) {
        JPanel p = new JPanel(new GridLayout(0, 2));
        p.add(new JLabel(s));
        p.add(field);
        return p;
    }

    private void updateView() {
        scale.setText(String.valueOf(0.0));
    }

    @Override
    public void onNotify(AffineTransform3D transform) {
        this.transform = transform;
        updateView();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        double x = Double.parseDouble(scale.getText());
        this.transform.scale(x);
        updater.setTransformation(this.transform);
        updateView();
    }
}