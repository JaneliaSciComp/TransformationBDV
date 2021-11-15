package com.preibisch.bdvtransform.panels;

import com.preibisch.bdvtransform.panels.utils.TransformationUpdater;
import net.imglib2.realtransform.AffineTransform3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TranslationPanel extends BDVCardPanel implements ActionListener {
    private final TransformationUpdater updater;
    private AffineTransform3D transform;
    private JTextField tx;
    private JTextField ty;
    private JTextField tz;


    public TranslationPanel(AffineTransform3D transform, TransformationUpdater updater) {
        super("TranslationPanel",
                "Translate", new GridLayout(0, 1));
        this.transform = transform;
        this.updater = updater;
        double[] translations = transform.getTranslation();
        this.tx = new JTextField(String.valueOf(translations[0]));
        this.ty = new JTextField(String.valueOf(translations[1]));
        this.tz = new JTextField(String.valueOf(translations[2]));
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

    private void updateView() {
        double[] translations = transform.getTranslation();
        tx.setText(String.valueOf(translations[0]));
        ty.setText(String.valueOf(translations[1]));
        tz.setText(String.valueOf(translations[2]));
    }

    @Override
    public void onNotify(AffineTransform3D transform) {
        this.transform = transform;
        updateView();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        double x = Double.parseDouble(tx.getText());
        double y = Double.parseDouble(ty.getText());
        double z = Double.parseDouble(tz.getText());
        double[] newTranslation = {x, y, z};
        this.transform.setTranslation(newTranslation);
        updater.setTransformation(this.transform,this);
    }
}
