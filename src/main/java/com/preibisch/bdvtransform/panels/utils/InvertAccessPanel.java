package com.preibisch.bdvtransform.panels.utils;

import com.preibisch.bdvtransform.panels.BDVCardPanel;
import net.imglib2.realtransform.AffineTransform3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InvertAccessPanel extends BDVCardPanel implements ActionListener {
    private final TransformationUpdater updater;
    private AffineTransform3D transform;
    private JTextField px;
    private JTextField py;
    private JTextField pz;


    public InvertAccessPanel(AffineTransform3D transform, TransformationUpdater updater) {
        super(new GridLayout(0, 1));
        this.transform = transform;
        this.updater = updater;
        this.px = new JTextField(String.valueOf(0));
        this.py = new JTextField(String.valueOf(1));
        this.pz = new JTextField(String.valueOf(2));
        this.add(getPanel("X: ", this.px));
        this.add(getPanel("Y: ", this.py));
        this.add(getPanel("Z: ", this.pz));
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
        this.px = new JTextField(String.valueOf(0));
        this.py = new JTextField(String.valueOf(1));
        this.pz = new JTextField(String.valueOf(2));
    }

    @Override
    public void onNotify(AffineTransform3D transform) {
        this.transform = transform;
        updateView();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int x = Integer.parseInt(px.getText());
        int y = Integer.parseInt(py.getText());
        int z = Integer.parseInt(pz.getText());
//        transform.set
//        transform.set(0.0,1.0,0.0,0.0,0.0,0.0,1.0,0.0,1.0,0.0,0.0,0.0);
//        double[] newTranslation = {x, y, z};
//        translationAction.setTranslation(newTranslation);
    }
}