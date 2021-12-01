package com.preibisch.bdvtransform.panels.utils.tansformation;

import bdv.tools.transformation.TransformedSource;
import bdv.util.BdvStackSource;
import bdv.viewer.SourceAndConverter;
import com.preibisch.bdvtransform.panels.utils.MathUtils;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.NumericType;

import java.util.ArrayList;
import java.util.List;

public class MultiSourceTransformations {
    private List<ImageTransformations> sourcesTransformation = new ArrayList<>();
    int currentSource = 0;
    private AffineTransform3D tmpTransform;

    public MultiSourceTransformations(int numberOfSources) {
        for (int i = 0; i < numberOfSources; i++) {
            sourcesTransformation.add(new ImageTransformations());
        }
    }

    public MultiSourceTransformations(List<AffineTransform3D> transforms) {
        for (AffineTransform3D transform :
                transforms) {
            sourcesTransformation.add(new ImageTransformations(transform));
        }
    }

    public <T extends NumericType<T> & NativeType<T>> void updateView(BdvStackSource<T> bdv) {
        ((TransformedSource<?>) bdv.getSources().get(currentSource).getSpimSource()).setFixedTransform(getCurrentTransformations().get());
        bdv.getBdvHandle().getViewerPanel().requestRepaint();
    }

    public void setCurrentSource(int currentSource) {
        this.currentSource = currentSource;
    }

    public int getCurrentSource() {
        return currentSource;
    }

    public ImageTransformations getCurrentTransformations() {
        return sourcesTransformation.get(currentSource);
    }

    public static <T extends NumericType<T> & NativeType<T>> MultiSourceTransformations initWithSources(List<SourceAndConverter<T>> sources) {
        List<AffineTransform3D> transforms = new ArrayList<>();
        for (int i = 0; i < sources.size(); i++) {
            AffineTransform3D transformation = new AffineTransform3D();
            sources.get(i).getSpimSource().getSourceTransform(0, 0, transformation);
            transforms.add(transformation);
        }
        return new MultiSourceTransformations(transforms);
    }

    public <T extends NumericType<T> & NativeType<T>> void addManualTransformationFrom(BdvStackSource<T> bdv) {
        AffineTransform3D newTransform = new AffineTransform3D();
        bdv.getSources().get(currentSource).getSpimSource().getSourceTransform(0, 0, newTransform);
         newTransform.preConcatenate(tmpTransform.inverse());
         newTransform.set(MathUtils.round(newTransform.getRowPackedCopy()));
        getCurrentTransformations().addManual(newTransform);
    }

    public <T extends NumericType<T> & NativeType<T>> void startManualTransform(BdvStackSource<T> bdv) {
        tmpTransform  = new AffineTransform3D();
        bdv.getSources().get(currentSource).getSpimSource().getSourceTransform(0, 0, tmpTransform);
    }
}
