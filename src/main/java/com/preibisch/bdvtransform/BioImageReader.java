package com.preibisch.bdvtransform;

import ij.ImagePlus;
import io.IOHelper;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.ImagePlusAdapter;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.NumericType;

import java.io.File;
import java.io.IOException;

public class BioImageReader {
    public static <T extends NumericType<T> & NativeType<T>> RandomAccessibleInterval<T> loadImage(String path) throws IOException {
        File file = new File(path);
        if (!file.exists())
            throw new IOException("File not found! Wrong path: " + file.getAbsolutePath());

        System.out.println("Opening: " + file.getAbsolutePath());
        ImagePlus imp = new IOHelper().readIp(file);
        final Img<T> image = ImagePlusAdapter.wrap(imp);
        System.out.println("Got the image.");
        return image;
    }

}
