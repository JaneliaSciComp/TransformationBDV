package com.preibisch.bdvtransform.panels.utils;

import net.imglib2.realtransform.AffineTransform3D;

public interface TransformationUpdater {
     void setTransformation(AffineTransform3D transformation);
}
