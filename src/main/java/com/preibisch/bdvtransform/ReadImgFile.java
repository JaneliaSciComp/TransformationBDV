package com.preibisch.bdvtransform;

import ij.ImagePlus;
import io.IOHelper;
import net.imglib2.img.ImagePlusAdapter;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.NumericType;

import java.io.File;
import java.io.IOException;

public class ReadImgFile {
    private static final String TEST_IMAGE_PATH = "/Users/Marwan/Desktop/cellmap/Task/data/PALM_532nm_gauss3d_d2.nrrd";

    public static < T extends NumericType< T > & NativeType< T >> void main(String[] args) throws IOException {
        File file = new File( TEST_IMAGE_PATH );
        if (!file.exists())
            throw new IOException("File not found! Wrong path: "+file.getAbsolutePath());

        System.out.println("Opening: "+file.getAbsolutePath());
        ImagePlus imp = new IOHelper().readIp(file);
//        ImagePlus imp = IJ.openImage(file.getAbsolutePath());
        imp.show();

        // wrap it into an ImgLib image (no copying)
        final Img< T > image = ImagePlusAdapter.wrap( imp );
    }
}
