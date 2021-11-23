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
    private JTextField sx;
    private JTextField sy;
    private JTextField sz;


    public ScalingPanel(AffineTransform3D transform, TransformationUpdater updater) {
        super("ScalingPanel", "Scale", new GridLayout(0, 1));
        this.transform = transform;
        this.updater = updater;
        this.sx = new JTextField(String.valueOf(1.0));
        this.sy = new JTextField(String.valueOf(1.0));
        this.sz = new JTextField(String.valueOf(1.0));
        this.add(getPanel("Rx: ", this.sx));
        this.add(getPanel("Ry: ", this.sy));
        this.add(getPanel("Rz: ", this.sz));
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        this.add(updateButton);
    }

    private JPanel getPanel(String s, JTextField field) {
        JPanel p = new JPanel(new GridLayout(0, 3));
        p.add(new JLabel(s));
        p.add(field);
        p.add(new Label("/1.0"));
        return p;
    }

    private void updateView() {
        this.sx.setText(String.valueOf(1.0));
        this.sy.setText(String.valueOf(1.0));
        this.sz.setText(String.valueOf(1.0));
    }

    @Override
    public void onNotify(AffineTransform3D transform) {
        this.transform = transform;
        updateView();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        double x = Double.parseDouble(sx.getText());
        double y = Double.parseDouble(sy.getText());
        double z = Double.parseDouble(sz.getText());
        this.transform.scale(x,y,z);
        updater.setTransformation(this.transform,this);
        updateView();
    }
}