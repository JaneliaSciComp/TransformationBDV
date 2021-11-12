package com.preibisch.bdvtransform.panels;

import net.imglib2.realtransform.AffineTransform3D;

import javax.swing.*;
import java.awt.*;

public abstract class BDVCardPanel extends JPanel {
    final private String key;
    final private String title;

    public BDVCardPanel(String key, String title, LayoutManager layout) {
        super(layout);
        this.key = key;
        this.title = title;
    }

    public abstract void onNotify(AffineTransform3D transform);

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }
}
