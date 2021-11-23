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
    private JTextField sx;
    private JTextField sy;
    private JTextField sz;


    public ScalingPanel(TransformationUpdater updater) {
        super("ScalingPanel", "Scale", new GridLayout(0, 1));
        this.updater = updater;
        this.sx = new JTextField(String.valueOf(1.0));
        this.sy = new JTextField(String.valueOf(1.0));
        this.sz = new JTextField(String.valueOf(1.0));
        this.oneScale = new JCheckBox();
        oneScale.addItemListener(e -> {
            modeChanged(oneScale.isSelected());
        });
        this.add(getPanel("", oneScale, "Same scale", "", ""));
        this.add(getPanel("Sx: ", this.sx, "/1.0"));
        this.add(getPanel("Sy: ", this.sy, "/1.0"));
        this.add(getPanel("Sz: ", this.sz, "/1.0"));
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        this.add(updateButton);
    }

    private void modeChanged(boolean oneScale) {
        setEditableOptions(sy, oneScale);
        setEditableOptions(sz, oneScale);
    }

    private void setEditableOptions(JTextField field, boolean oneScale) {
        field.setEditable(!oneScale);
        if (oneScale)
            field.setForeground(new Color(189, 195, 199));
        else
            field.setForeground(new Color(0, 0, 0));
    }

    private JPanel getPanel(Object... elements) {
        JPanel p = new JPanel(new GridLayout(0, elements.length));
        for (Object element : elements) {
            if (element.getClass().equals(String.class))
                p.add(new JLabel((String) element));
            else
                p.add((JComponent) element);
        }
        return p;
    }

    @Override
    protected void updateView() {
        this.sx.setText(String.valueOf(1.0));
        this.sy.setText(String.valueOf(1.0));
        this.sz.setText(String.valueOf(1.0));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AffineTransform3D transform = new AffineTransform3D();
        if (oneScale.isSelected()) {
            double x = Double.parseDouble(sx.getText());
            transform.scale(x);
        } else {
            double x = Double.parseDouble(sx.getText());
            double y = Double.parseDouble(sy.getText());
            double z = Double.parseDouble(sz.getText());
            transform.scale(x, y, z);
        }
        updater.setTransformation(transform, this);
        updateView();
    }
}