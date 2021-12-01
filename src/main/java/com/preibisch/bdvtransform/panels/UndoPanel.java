package com.preibisch.bdvtransform.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class UndoPanel extends BDVCardPanel {

    public UndoPanel(ActionListener listener) {
        super("UndoPanel", "Undo", new GridLayout(0, 1));

        JButton updateButton = new JButton("Undo");
        updateButton.addActionListener(listener);
        this.add(updateButton);
    }

}