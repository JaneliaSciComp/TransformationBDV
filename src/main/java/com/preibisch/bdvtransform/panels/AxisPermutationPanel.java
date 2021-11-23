package com.preibisch.bdvtransform.panels;

import com.preibisch.bdvtransform.panels.utils.TransformationUpdater;
import net.imglib2.realtransform.AffineTransform3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AxisPermutationPanel extends BDVCardPanel implements ActionListener {
    private final TransformationUpdater updater;
    private JTextField px;
    private JTextField py;
    private JTextField pz;


    public AxisPermutationPanel(TransformationUpdater updater) {
        super("AxisPermutationPanel", "Permute Axis", new GridLayout(0, 1));

        this.updater = updater;
        this.px = new JTextField(String.valueOf(0));
        this.py = new JTextField(String.valueOf(1));
        this.pz = new JTextField(String.valueOf(2));
        this.add(getPanel("Px: ", this.px));
        this.add(getPanel("Py: ", this.py));
        this.add(getPanel("Pz: ", this.pz));
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

    @Override
    protected void updateView() {
        this.px.setText(String.valueOf(0));
        this.py.setText(String.valueOf(1));
        this.pz.setText(String.valueOf(2));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int x = Integer.parseInt(px.getText());
            int y = Integer.parseInt(py.getText());
            int z = Integer.parseInt(pz.getText());
            double[] positionX = getPosition(x);
            double[] positionY = getPosition(y);
            double[] positionZ = getPosition(z);
            AffineTransform3D transform = new AffineTransform3D();
            double[][] permutationMatrix = {positionX, positionY, positionZ};
            transform.set(permutationMatrix);
            updater.setTransformation(transform, this);
            updateView();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private double[] getPosition(int x) throws Exception {
        switch (x) {
            case 0:
                return new double[]{1, 0, 0, 0};
            case 1:
                return new double[]{0, 1, 0, 0};
            case 2:
                return new double[]{0, 0, 1, 0};
            default:
                throw new Exception("Invalid axe position " + x);
        }
    }
}