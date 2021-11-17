package com.preibisch.bdvtransform;

import bdv.tools.transformation.TransformedSource;
import bdv.ui.BdvDefaultCards;
import bdv.ui.CardPanel;
import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import bdv.util.BdvStackSource;
import bdv.viewer.Source;
import bdv.viewer.ViewerStateChange;
import com.preibisch.bdvtransform.panels.AxisPermutationPanel;
import com.preibisch.bdvtransform.panels.BDVCardPanel;
import com.preibisch.bdvtransform.panels.ExportTransformationPanel;
import com.preibisch.bdvtransform.panels.FlipPanel;
import com.preibisch.bdvtransform.panels.RandomColorPanel;
import com.preibisch.bdvtransform.panels.RotationPanel;
import com.preibisch.bdvtransform.panels.ScalingPanel;
import com.preibisch.bdvtransform.panels.TranslationPanel;
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
            bdv.getConverterSetups().addAll(source.getConverterSetups());
        }

        bdv.getBdvHandle().getViewerPanel().state().changeListeners().add(viewerStateChange -> {
            System.out.println("Viewer state changed: "+viewerStateChange.toString());
            if(viewerStateChange.name().equals(ViewerStateChange.CURRENT_SOURCE_CHANGED.name())){
                Source<?> currentSource = bdv.getBdvHandle().getViewerPanel().state().getCurrentSource().getSpimSource();
                for (int i = 0; i < bdv.getSources().size(); i++) {

                    if (bdv.getSources().get(i).getSpimSource().equals(currentSource)){
                        System.out.println("Current source is: "+currentSource.getName()+" | Position : "+i);
                        sourceId = i;
                        spimSource = bdv.getSources().get(i).getSpimSource();
                        notifyPanels();
                        break;
                    }
                }}
        });
        affineTransform3DList = new ArrayList<>();
        controlPanels = new ArrayList<>();
        for (int i = 0; i < paths.length; i++)
            affineTransform3DList.add(new AffineTransform3D());
        sourceId = 0;
        TransformationUpdater updater = (transformation, source) -> {
            System.out.println("New Transformation: ");
            MatrixOperation.print(MatrixOperation.toMatrix(transformation.getRowPackedCopy(), 4));
            affineTransform3DList.set(sourceId, transformation);
            ((TransformedSource<?>) spimSource).setFixedTransform(transformation);
            bdv.getBdvHandle().getViewerPanel().requestRepaint();
        };

        final CardPanel cardPanel = bdv.getBdvHandle().getCardPanel();

        cardPanel.addCard("ColorPanel", "RandomColor",
                new RandomColorPanel(bdv).click(),
                false,
                new Insets(0, 4, 0, 0));

        this.controlPanels.add(new TranslationPanel(affineTransform3DList.get(sourceId), updater));
        this.controlPanels.add(new ScalingPanel(affineTransform3DList.get(sourceId), updater));
        this.controlPanels.add(new RotationPanel(affineTransform3DList.get(sourceId), updater));
        this.controlPanels.add(new FlipPanel(affineTransform3DList.get(sourceId), updater));
        this.controlPanels.add(new AxisPermutationPanel(affineTransform3DList.get(sourceId), updater));
        this.controlPanels.add(new ExportTransformationPanel(affineTransform3DList.get(sourceId), updater));

        this.controlPanels.forEach(p -> cardPanel.addCard(p.getKey(),
                p.getTitle(),
                p,
                false,
                new Insets(0, 4, 0, 0)));

        cardPanel.setCardExpanded(BdvDefaultCards.DEFAULT_VIEWERMODES_CARD, false);
        cardPanel.setCardExpanded(BdvDefaultCards.DEFAULT_SOURCES_CARD, false);
        cardPanel.setCardExpanded(BdvDefaultCards.DEFAULT_SOURCEGROUPS_CARD, false);

        bdv.getBdvHandle().getViewerPanel().requestRepaint();
    }

    private void notifyPanels() {
        this.controlPanels.forEach(p -> p.onNotify(affineTransform3DList.get(sourceId)));
    }

    public static void main(String[] args) throws IOException {
        new BDVStacking(TEST_DATA.TEST_IMAGE1_PATH, TEST_DATA.TEST_IMAGE2_PATH);
    }
}
