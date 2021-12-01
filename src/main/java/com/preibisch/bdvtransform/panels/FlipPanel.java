package com.preibisch.bdvtransform.panels;

import com.preibisch.bdvtransform.panels.utils.tansformation.TransformationUpdater;
import net.imglib2.realtransform.AffineTransform3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FlipPanel extends BDVCardPanel implements ActionListener {
    private final TransformationUpdater updater;
    private JCheckBox fx;
    private JCheckBox fy;
    private JCheckBox fz;


    public FlipPanel(TransformationUpdater updater) {
        super("FlipPanel", "Flip", new GridLayout(0, 1));
        this.updater = updater;
        this.fx = new JCheckBox();
        this.fy = new JCheckBox();
        this.fz = new JCheckBox();
        this.add(getPanel("Tx: ", this.fx));
        this.add(getPanel("Ty: ", this.fy));
        this.add(getPanel("Tz: ", this.fz));
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        this.add(updateButton);
    }

    private JPanel getPanel(String s, JComponent field) {
        JPanel p = new JPanel(new GridLayout(0, 2));
        p.add(new JLabel(s));
        p.add(field);
        return p;
    }

    @Override
    protected void updateView() {
        fx.setSelected(false);
        fy.setSelected(false);
        fz.setSelected(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            AffineTransform3D transform = new AffineTransform3D();
            double[] x = putInList(0, getValue(fx.isSelected()));
            double[] y = putInList(1, getValue(fy.isSelected()));
            double[] z = putInList(2, getValue(fz.isSelected()));

            double[][] flipMatrix = {x, y, z};
            transform.set(flipMatrix);
            updater.setTransformation(transform, this);
            updateView();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private double getValue(boolean selected) {
        if (selected)
            return -1;
        else
            return 1;
    }

    private double[] putInList(int pos, double value) throws Exception {
        switch (pos) {
            case 0:
                return new double[]{value, 0, 0, 0};
            case 1:
                return new double[]{0, value, 0, 0};
            case 2:
                return new double[]{0, 0, value, 0};
            default:
                throw new Exception("Invalid axe position " + pos);
        }
    }
}