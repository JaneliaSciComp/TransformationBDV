package com.preibisch.bdvtransform.panels;

import com.preibisch.bdvtransform.panels.utils.tansformation.TransformationUpdater;
import net.imglib2.realtransform.AffineTransform3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TranslationPanel extends BDVCardPanel implements ActionListener {
    private final TransformationUpdater updater;
    private final JTextField tx;
    private final JTextField ty;
    private final JTextField tz;

    public TranslationPanel(TransformationUpdater updater) {
        super("TranslationPanel", "Translate", new GridLayout(0, 1));
        this.updater = updater;
        this.tx = new JTextField(String.valueOf(0.0));
        this.ty = new JTextField(String.valueOf(0.0));
        this.tz = new JTextField(String.valueOf(0.0));
        this.add(getPanel("Tx: ", this.tx));
        this.add(getPanel("Ty: ", this.ty));
        this.add(getPanel("Tz: ", this.tz));
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
        tx.setText(String.valueOf(0.0));
        ty.setText(String.valueOf(0.0));
        tz.setText(String.valueOf(0.0));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AffineTransform3D transform = new AffineTransform3D();
        double x = Double.parseDouble(tx.getText());
        double y = Double.parseDouble(ty.getText());
        double z = Double.parseDouble(tz.getText());
        double[] newTranslation = {x, y, z};
        transform.setTranslation(newTranslation);
        updater.setTransformation(transform, this);
        updateView();
    }
}
