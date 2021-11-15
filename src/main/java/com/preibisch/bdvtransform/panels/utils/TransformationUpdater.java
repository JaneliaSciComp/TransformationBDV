package com.preibisch.bdvtransform.panels.utils;

import com.preibisch.bdvtransform.panels.BDVCardPanel;
import net.imglib2.realtransform.AffineTransform3D;

import java.util.List;

public interface TransformationUpdater {
     void setTransformation(AffineTransform3D transformation, BDVCardPanel source);
     default void setAllTransformation(List<AffineTransform3D> allTransformation, BDVCardPanel source){
     }
}
