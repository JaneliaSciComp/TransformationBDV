package com.preibisch.bdvtransform.panels.utils;

import bdv.BigDataViewerActions;
import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import bdv.util.BdvStackSource;
import com.preibisch.bdvtransform.BioImageReader;
import com.preibisch.bdvtransform.TEST_DATA;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.NumericType;

import java.io.File;
import java.io.IOException;

public class TestClick<T extends NumericType<T> & NativeType<T>> {
    private final String image;

    public TestClick(String image) {
        this.image = image;
    }

    public static void main(String[] args) throws IOException {
        new TestClick(TEST_DATA.TEST_IMAGE1_PATH).run();
    }

    private void run() throws IOException {
        BdvOptions options = BdvOptions.options();
        BigDataViewerActions actions = new BigDataViewerActions(options.values.getInputTriggerConfig());
        actions.runnableAction((Runnable) () -> System.out.println("Hello"),"Random Color", new String[]{"R"});
        final BdvStackSource<T> bdv = BdvFunctions.show((RandomAccessibleInterval<T>) BioImageReader.loadImage(image), new File(image).getName(),options);
        actions.install( bdv.getBdvHandle().getKeybindings(), "my actions" );
    }
}
