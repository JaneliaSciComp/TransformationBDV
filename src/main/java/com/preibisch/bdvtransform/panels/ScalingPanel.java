package com.preibisch.bdvtransform.panels;

import com.preibisch.bdvtransform.panels.utils.TransformationUpdater;
import net.imglib2.realtransform.AffineTransform3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScalingPanel extends BDVCardPanel implements ActionListener {
    private final TransformationUpdater updater;
    private final JCheckBox oneScale;
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
        this.oneScale = new JCheckBox();
        oneScale.addItemListener(e -> {
            modeChanged(oneScale.isSelected());
        });
        this.add(getPanel("",oneScale, "Same scale","",""));
        this.add(getPanel("Sx: ", this.sx,"/1.0"));
        this.add(getPanel("Sy: ", this.sy,"/1.0"));
        this.add(getPanel("Sz: ", this.sz,"/1.0"));
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        this.add(updateButton);
    }

    private void modeChanged(boolean selected) {
    }

    private JPanel getPanel(Object... elements) {
        JPanel p = new JPanel(new GridLayout(0, elements.length));
        for (Object element :
                elements) {
            if (element.getClass().equals(String.class))
                p.add(new JLabel((String) element));
            else
                p.add((JComponent) element);
        }
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
        this.transform.scale(x, y, z);
        updater.setTransformation(this.transform, this);
        updateView();
    }
}