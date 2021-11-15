package com.preibisch.bdvtransform;

import bdv.tools.brightness.ConverterSetup;
import bdv.util.BdvHandle;
import bdv.util.BdvStackSource;
import com.preibisch.bdvtransform.panels.BDVCardPanel;
import com.preibisch.bdvtransform.panels.utils.ColorStream;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.NumericType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

public class RandomColorPanel extends BDVCardPanel implements ActionListener {

    private final Iterator<ARGBType> iterator;
    private final List<ConverterSetup> setups;
    private final BdvHandle handler;


    public <T extends NumericType<T> & NativeType<T>> RandomColorPanel(BdvStackSource<T> bdv) {
        super("ColorPanel", "RandomColor", new GridLayout(0, 1));
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        this.iterator = ColorStream.iterator();
        iterator.next();
        this.setups = bdv.getConverterSetups();
        this.handler = bdv.getBdvHandle();
        this.add(updateButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Change Color for " + setups.size() + " sources.");
        colorSources(setups, iterator);
        handler.getViewerPanel().requestRepaint();
    }

    public static void colorSources(List<ConverterSetup> setups, Iterator<ARGBType> iterator) {

        for (int i = 0; i < setups.size(); ++i)
            setups.get(i).setColor(iterator.next());
    }

    @Override
    public void onNotify(AffineTransform3D transform) {

    }

    public RandomColorPanel click(){
        this.actionPerformed(null);
        return this;
    }
}