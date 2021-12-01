package com.preibisch.bdvtransform.panels;

import net.imglib2.realtransform.AffineTransform3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class ExportTransformationPanel extends BDVCardPanel {

    public ExportTransformationPanel(ActionListener listener, boolean expend) {
        super("ExportPanel", "Export Transformation", new GridLayout(0, 1), expend);
        JButton updateButton = new JButton("Save");
        updateButton.addActionListener(listener);
        this.add(updateButton);
    }

    public ExportTransformationPanel(ActionListener listener) {
        this(listener, false);
    }

    public static void save(AffineTransform3D transform) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(null);
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