package com.preibisch.bdvtransform.panels;

import com.preibisch.bdvtransform.panels.utils.TransformationUpdater;
import net.imglib2.realtransform.AffineTransform3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class ExportTransformationPanel extends BDVCardPanel implements ActionListener {
    private final TransformationUpdater updater;
    private AffineTransform3D transform;


    public ExportTransformationPanel(AffineTransform3D transform, TransformationUpdater updater) {
        super("ExportPanel", "Export Transformation", new GridLayout(0, 1));
        this.transform = transform;
        this.updater = updater;
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        this.add(updateButton);
    }

    @Override
    public void onNotify(AffineTransform3D transform) {
        this.transform = transform;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                double[] transformation = transform.getRowPackedCopy();
                for (int i = 0; i < transformation.length; i++) {
                    writer.write(transformation[i] + " ");
                }
                writer.flush();
                writer.close();
            }
        } catch (Exception exception) {
            System.out.println("Couldn't save the file " + exception.getMessage());
        }
    }
}