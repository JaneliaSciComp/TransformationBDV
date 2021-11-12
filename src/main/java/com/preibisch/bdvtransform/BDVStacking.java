package com.preibisch.bdvtransform;

import bdv.tools.transformation.TransformedSource;
import bdv.ui.BdvDefaultCards;
import bdv.ui.CardPanel;
import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import bdv.util.BdvStackSource;
import bdv.viewer.Source;
import com.preibisch.bdvtransform.panels.AxisPermutationPanel;
import com.preibisch.bdvtransform.panels.BDVCardPanel;
import com.preibisch.bdvtransform.panels.RotationPanel;
import com.preibisch.bdvtransform.panels.ScalingPanel;
import com.preibisch.bdvtransform.panels.SourceSelectionPanel;
import com.preibisch.bdvtransform.panels.TranslationPanel;
import com.preibisch.bdvtransform.panels.FlipPanel;
import com.preibisch.bdvtransform.panels.utils.MatrixOperation;
import com.preibisch.bdvtransform.panels.utils.TransformationUpdater;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.NumericType;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BDVStacking<T extends NumericType<T> & NativeType<T>> {
    private final List<BDVCardPanel> controlPanels;

    private Source<?> spimSource;
    private int sourceId;
    private List<AffineTransform3D> affineTransform3DList;

    public BDVStacking(String... paths) throws IOException {
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        final BdvStackSource<T> bdv = BdvFunctions.show((RandomAccessibleInterval<T>) BioImageReader.loadImage(paths[0]), new File(paths[0]).getName());
        spimSource = bdv.getSources().get(0).getSpimSource();

        for (int i = 1; i < paths.length; i++) {
            final RandomAccessibleInterval<T> img = BioImageReader.loadImage(paths[i]);
            BdvStackSource<T> source = BdvFunctions.show(img, new File(paths[i]).getName(), BdvOptions.options().addTo(bdv));
            bdv.getSources().addAll(source.getSources());
        }
        affineTransform3DList = new ArrayList<>();
        controlPanels = new ArrayList<>();
        for (int i = 0; i < paths.length; i++)
            affineTransform3DList.add(new AffineTransform3D());
        sourceId = 0;

////            transform3D.scale(4);
//            transform3D.translate();
////                            transform3D.setTranslation(1.027752,0.142380,0.003838,-0.111033,1.086820,0.001491,0.006316,-0.055135,0.990582,1.065162,1.774045,1.281561);
//        ((TransformedSource<?>) spimSource).setFixedTransform(transform3D);
//        transform3D.set(0.0,1.0,0.0,0.0,0.0,0.0,1.0,0.0,1.0,0.0,0.0,0.0);

        final CardPanel cardPanel = bdv.getBdvHandle().getCardPanel();

        cardPanel.addCard("SelectSource",
                "Select Source",
                new SourceSelectionPanel(bdv.getSources(), i -> {
                    sourceId = i;
                    spimSource = bdv.getSources().get(i).getSpimSource();
                    notifyPanels();
                }),
                false,
                new Insets(0, 4, 0, 0));

        TransformationUpdater updater = transformation -> {
            System.out.println("New Transformation");
            MatrixOperation.print(MatrixOperation.toMatrix(transformation.getRowPackedCopy(),4));
            affineTransform3DList.set(sourceId, transformation);
            ((TransformedSource<?>) spimSource).setFixedTransform(transformation);
            bdv.getBdvHandle().getViewerPanel().requestRepaint();
        };

        this.controlPanels.add(new TranslationPanel(affineTransform3DList.get(sourceId), updater));
        this.controlPanels.add(new ScalingPanel(affineTransform3DList.get(sourceId), updater));
        this.controlPanels.add(new RotationPanel(affineTransform3DList.get(sourceId), updater));
        this.controlPanels.add(new FlipPanel(affineTransform3DList.get(sourceId), updater));
        this.controlPanels.add(new AxisPermutationPanel(affineTransform3DList.get(sourceId), updater));

        this.controlPanels.forEach(p -> cardPanel.addCard(p.getKey(),
                p.getTitle(),
                p,
                false,
                new Insets(0, 4, 0, 0)));

        cardPanel.setCardExpanded(BdvDefaultCards.DEFAULT_VIEWERMODES_CARD, false);
        cardPanel.setCardExpanded(BdvDefaultCards.DEFAULT_SOURCES_CARD, false);
        cardPanel.setCardExpanded(BdvDefaultCards.DEFAULT_SOURCEGROUPS_CARD, false);
    }

    private void notifyPanels() {
        this.controlPanels.forEach(p -> p.onNotify(affineTransform3DList.get(sourceId)));
    }

    public static void main(String[] args) throws IOException {
        new BDVStacking(TEST_DATA.TEST_IMAGE1_PATH, TEST_DATA.TEST_IMAGE2_PATH);
    }

}
