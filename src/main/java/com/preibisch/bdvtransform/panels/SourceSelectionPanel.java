package com.preibisch.bdvtransform.panels;

import bdv.viewer.SourceAndConverter;
import com.preibisch.bdvtransform.panels.utils.SourceSelector;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.NumericType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

public class SourceSelectionPanel extends JPanel implements ActionListener {
    private final SourceSelector sourceSelector;

    public <T extends NumericType< T > & NativeType< T >> SourceSelectionPanel(List<SourceAndConverter<T>> sources, SourceSelector sourceSelector) {

        super(new GridLayout(0,1));
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < sources.size(); i++) {
            String name = sources.get(i).getSpimSource().getName();
            JRadioButton button = new JRadioButton(name);
            button.setMnemonic(KeyEvent.getExtendedKeyCodeForChar((char) i));
            button.setActionCommand(String.valueOf(i));
            button.addActionListener(this);
            if (i == 0)
                button.setSelected(true);
            group.add(button);
            this.add(button);

        }
        this.sourceSelector = sourceSelector;
    }
       
        

        public void actionPerformed(ActionEvent e) {
           sourceSelector.setSource(Integer.parseInt(e.getActionCommand()));
        }

}
