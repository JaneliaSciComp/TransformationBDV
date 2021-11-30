package com.preibisch.bdvtransform.panels.utils.tansformation;

import net.imglib2.realtransform.AffineTransform3D;

public class ImageTransformation {
    final private AffineTransform3D transformation;
    final private TransformationType type;

    public ImageTransformation(AffineTransform3D transformation, TransformationType type) {
        this.transformation = transformation;
        this.type = type;
    }

    public ImageTransformation(AffineTransform3D transform) {
        this(transform, TransformationType.Automatic);
    }

    public AffineTransform3D getTransformation() {
        return transformation;
    }

    public TransformationType getType() {
        return type;
    }
}
