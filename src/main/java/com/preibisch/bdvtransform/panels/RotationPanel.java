package com.preibisch.bdvtransform.panels;

import com.preibisch.bdvtransform.panels.utils.TransformationUpdater;
import net.imglib2.realtransform.AffineTransform3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RotationPanel extends BDVCardPanel implements ActionListener {
    private final TransformationUpdater updater;
    private AffineTransform3D transform;
    private JTextField rx;
    private JTextField ry;
    private JTextField rz;


    public RotationPanel(AffineTransform3D transform, TransformationUpdater updater) {
        super("RotationPanel", "Rotate", new GridLayout(0, 1));
        this.transform = transform;
        this.updater = updater;
        this.rx = new JTextField(String.valueOf(0.0));
        this.ry = new JTextField(String.valueOf(0.0));
        this.rz = new JTextField(String.valueOf(0.0));
        this.add(getPanel("Rx: ", this.rx));
        this.add(getPanel("Ry: ", this.ry));
        this.add(getPanel("Rz: ", this.rz));
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
        this.rx.setText(String.valueOf(0.0));
        this.ry.setText(String.valueOf(0.0));
        this.rz.setText(String.valueOf(0.0));
    }

    @Override
    public void onNotify(AffineTransform3D transform) {
        this.transform = transform;
        updateView();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        double x = Double.parseDouble(rx.getText());
        double y = Double.parseDouble(ry.getText());
        double z = Double.parseDouble(rz.getText());
        if (x > 0) this.transform.rotate(0, x);
        if (y > 0) this.transform.rotate(1, y);
        if (z > 0) this.transform.rotate(2, z);
        updater.setTransformation(this.transform);
        updateView();
    }
}