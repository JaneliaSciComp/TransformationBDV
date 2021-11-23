package com.preibisch.bdvtransform;

import bdv.BigDataViewerActions;
import bdv.tools.transformation.TransformedSource;
import bdv.ui.BdvDefaultCards;
import bdv.ui.CardPanel;
import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import bdv.util.BdvStackSource;
import com.preibisch.bdvtransform.panels.AxisPermutationPanel;
import com.preibisch.bdvtransform.panels.BDVCardPanel;
import com.preibisch.bdvtransform.panels.ExportTransformationPanel;
import com.preibisch.bdvtransform.panels.FlipPanel;
import com.preibisch.bdvtransform.panels.RandomColorPanel;
import com.preibisch.bdvtransform.panels.RotationPanel;
import com.preibisch.bdvtransform.panels.ScalingPanel;
import com.preibisch.bdvtransform.panels.TranslationPanel;
import com.preibisch.bdvtransform.panels.UndoPanel;
import com.preibisch.bdvtransform.panels.utils.BDVUtils;
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
    private final List<BDVCardPanel> controlPanels = new ArrayList<>();
    private int sourceId = 0;
    final private List<AffineTransform3D> affineTransform3DList = new ArrayList<>();
    private AffineTransform3D oldTransform;

    public BDVStacking(String... paths) throws IOException {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        BdvOptions options = BdvOptions.options();
        final BdvStackSource<T> bdv = BdvFunctions.show((RandomAccessibleInterval<T>) BioImageReader.loadImage(paths[0]), new File(paths[0]).getName(), options);

        for (int i = 1; i < paths.length; i++) {
            final RandomAccessibleInterval<T> img = BioImageReader.loadImage(paths[i]);
            BdvStackSource<T> source = BdvFunctions.show(img, new File(paths[i]).getName(), BdvOptions.options().addTo(bdv));
            bdv.getSources().addAll(source.getSources());
            bdv.getConverterSetups().addAll(source.getConverterSetups());
        }

        BDVUtils.initBrightness(bdv);

        bdv.getBdvHandle().getViewerPanel().state().changeListeners().add(viewerStateChange -> {
            if (bdv.getBdvHandle().getViewerPanel().state().getCurrentSource() == null)
                return;

            for (int i = 0; i < bdv.getSources().size(); i++)
                if (bdv.getSources().get(i).getSpimSource().equals(bdv.getBdvHandle().getViewerPanel().state().getCurrentSource().getSpimSource())) {
                    System.out.println("Current source position : " + i);
                    sourceId = i;
                    notifyPanels();
                    oldTransform = affineTransform3DList.get(sourceId);
                    break;
                }
        });

        for (int i = 0; i < paths.length; i++) {
            AffineTransform3D transformation = new AffineTransform3D();
            bdv.getSources().get(i).getSpimSource().getSourceTransform(0, 0, transformation);
            affineTransform3DList.add(transformation);
        }

        oldTransform = affineTransform3DList.get(sourceId);
        TransformationUpdater updater = (transformation, source) -> {
            oldTransform = affineTransform3DList.get(sourceId);
            affineTransform3DList.set(sourceId, transformation);
            updateSourceTransformation(bdv, sourceId, transformation);
        };

        bdv.getBdvHandle().getManualTransformEditor().manualTransformActiveListeners().add(b -> {
            if (!b) {
                System.out.println("Manual Transformed: ");
                AffineTransform3D newTransform = new AffineTransform3D();
                bdv.getSources().get(0).getSpimSource().getSourceTransform(0, 0, newTransform);
                MatrixOperation.print(MatrixOperation.toMatrix(newTransform.getRowPackedCopy(), 4));
                oldTransform = affineTransform3DList.get(sourceId);
                affineTransform3DList.set(sourceId, newTransform);
                notifyPanels();
            }
        });

        final CardPanel cardPanel = bdv.getBdvHandle().getCardPanel();

        AffineTransform3D currentTransformation = affineTransform3DList.get(sourceId).copy();
        this.controlPanels.add(new TranslationPanel(currentTransformation, updater));
        this.controlPanels.add(new ScalingPanel(currentTransformation, updater));
        this.controlPanels.add(new RotationPanel(currentTransformation, updater));
        this.controlPanels.add(new FlipPanel(currentTransformation, updater));
        this.controlPanels.add(new AxisPermutationPanel(currentTransformation, updater));
        this.controlPanels.add(new ExportTransformationPanel(currentTransformation, updater));

        RandomColorPanel randomColor = new RandomColorPanel(bdv).click();
        cardPanel.addCard("ColorPanel", "RandomColor",
                randomColor,
                false,
                new Insets(0, 4, 0, 0));

        cardPanel.addCard("UndoPanel", "Undo", new UndoPanel(e -> {
                    affineTransform3DList.set(sourceId, oldTransform);
                    ((TransformedSource<?>) bdv.getSources().get(sourceId).getSpimSource()).setFixedTransform(affineTransform3DList.get(sourceId));
                    bdv.getBdvHandle().getViewerPanel().requestRepaint();
                    notifyPanels();
                }), true,
                new Insets(0, 4, 0, 0));

        this.controlPanels.forEach(p -> cardPanel.addCard(p.getKey(),
                p.getTitle(),
                p,
                false,
                new Insets(0, 4, 0, 0)));

        BigDataViewerActions actions = new BigDataViewerActions(options.values.getInputTriggerConfig());
        actions.runnableAction(randomColor::click, "Random Color", "R");
        actions.install(bdv.getBdvHandle().getKeybindings(), "my actions");

        cardPanel.setCardExpanded(BdvDefaultCards.DEFAULT_VIEWERMODES_CARD, false);
        cardPanel.setCardExpanded(BdvDefaultCards.DEFAULT_SOURCES_CARD, false);
        cardPanel.setCardExpanded(BdvDefaultCards.DEFAULT_SOURCEGROUPS_CARD, false);

        bdv.getBdvHandle().getViewerPanel().requestRepaint();
    }

    private void updateSourceTransformation(BdvStackSource<T> bdv, int sourceId, AffineTransform3D transformation) {
        System.out.println("New Transformation: ");
        MatrixOperation.print(MatrixOperation.toMatrix(transformation.getRowPackedCopy(), 4));
        ((TransformedSource<?>) bdv.getSources().get(sourceId).getSpimSource()).setFixedTransform(transformation);
        bdv.getBdvHandle().getViewerPanel().requestRepaint();
        notifyPanels();
    }

    private void notifyPanels() {
        AffineTransform3D currentTransformation = affineTransform3DList.get(sourceId).copy();
        this.controlPanels.forEach(p -> p.onNotify(currentTransformation));
    }

    public static void main(String[] args) throws IOException {
        new BDVStacking(TEST_DATA.TEST_IMAGE1_PATH, TEST_DATA.TEST_IMAGE2_PATH);
    }
}
