package com.preibisch.bdvtransform.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class UndoPanel extends JPanel {

    public UndoPanel(ActionListener listener) {
        super(new GridLayout(0, 1));

        JButton updateButton = new JButton("Undo");
        updateButton.addActionListener(listener);
        this.add(updateButton);
    }

}