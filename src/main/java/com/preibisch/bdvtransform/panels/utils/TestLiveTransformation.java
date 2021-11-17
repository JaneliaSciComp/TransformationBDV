package com.preibisch.bdvtransform.panels.utils;

import bdv.tools.transformation.ManualTransformActiveListener;
import bdv.util.BdvFunctions;
import bdv.util.BdvStackSource;
import bdv.viewer.ViewerStateChange;
import com.preibisch.bdvtransform.BioImageReader;
import com.preibisch.bdvtransform.TEST_DATA;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.NumericType;

import java.io.File;
import java.io.IOException;

public class TestLiveTransformation<T extends NumericType<T> & NativeType<T>> {
    private final String image;
    private final AffineTransform3D transform;
    private boolean manualTransformation;

    public TestLiveTransformation(String image) {
        this.image = image;
        this.manualTransformation = false;
        this.transform = new AffineTransform3D();
    }

    public static void main(String[] args) throws IOException {
        new TestLiveTransformation(TEST_DATA.TEST_IMAGE1_PATH).run();
    }

    private void run() throws IOException {
        final BdvStackSource<T> bdv = BdvFunctions.show((RandomAccessibleInterval<T>) BioImageReader.loadImage(image), new File(image).getName());
        bdv.getBdvHandle().getManualTransformEditor().manualTransformActiveListeners().add(new ManualTransformActiveListener() {
            @Override
            public void manualTransformActiveChanged(boolean b) {
                System.out.println("Manual transform changed : " + b);
                manualTransformation = b;
            }
        });
        bdv.getBdvHandle().getViewerPanel().state().changeListeners().add(viewerStateChange -> {
//            System.out.println("Viewer state changed: " + viewerStateChange.toString());
            if (viewerStateChange.name().equals(ViewerStateChange.VIEWER_TRANSFORM_CHANGED.name()) && (manualTransformation)) {
                AffineTransform3D outTransform = new AffineTransform3D();
                bdv.getSources().get(0).getSpimSource().getSourceTransform(0, 0, outTransform);
                MatrixOperation.print(outTransform.getRowPackedCopy());
            }
        });
    }
}


