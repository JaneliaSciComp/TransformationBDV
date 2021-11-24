package com.preibisch.bdvtransform.panels.utils.tansformation;

import com.preibisch.bdvtransform.panels.BDVCardPanel;
import net.imglib2.realtransform.AffineTransform3D;

public interface TransformationUpdater {
     void setTransformation(AffineTransform3D transformation, BDVCardPanel source);
}
