package com.preibisch.bdvtransform.panels;

import net.imglib2.realtransform.AffineTransform3D;

import javax.swing.*;
import java.awt.*;

public abstract class BDVCardPanel extends JPanel {
    public BDVCardPanel(LayoutManager layout) {
        super(layout);
    }

    public abstract void onNotify(AffineTransform3D transform);
}
