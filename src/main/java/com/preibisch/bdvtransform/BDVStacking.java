package com.preibisch.bdvtransform;

import bdv.BigDataViewerActions;
import bdv.ui.BdvDefaultCards;
import bdv.ui.CardPanel;
import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import bdv.util.BdvStackSource;
import bdv.viewer.ViewerStateChange;
import com.preibisch.bdvtransform.panels.AxisPermutationPanel;
import com.preibisch.bdvtransform.panels.BDVCardPanel;
import com.preibisch.bdvtransform.panels.ExportTransformationPanel;
import com.preibisch.bdvtransform.panels.FlipPanel;
import com.preibisch.bdvtransform.panels.RotationPanel;
import com.preibisch.bdvtransform.panels.ScalingPanel;
import com.preibisch.bdvtransform.panels.TransformationsHistoryPanel;
import com.preibisch.bdvtransform.panels.TranslationPanel;
import com.preibisch.bdvtransform.panels.utils.bdv.BDVUtils;
import com.preibisch.bdvtransform.panels.utils.tansformation.MultiSourceTransformations;
import com.preibisch.bdvtransform.panels.utils.tansformation.TransformationUpdater;
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
    private final MultiSourceTransformations sourcesTransformations;
    private final TransformationsHistoryPanel transformationHistoryPanel;

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

        this.sourcesTransformations = MultiSourceTransformations.initWithSources(bdv.getSources());
        transformationHistoryPanel = new TransformationsHistoryPanel(sourcesTransformations.getCurrentTransformations(), position -> {
            if (sourcesTransformations.getCurrentTransformations().size() == 1) {
                bdv.getBdvHandle().getViewerPanel().showMessage("Can't remove Transformation");
                return false;
            }
            sourcesTransformations.getCurrentTransformations().removeAt(position);
            sourcesTransformations.updateView(bdv);
            bdv.getBdvHandle().getViewerPanel().requestRepaint();
            bdv.getBdvHandle().getViewerPanel().showMessage("Transformation " + position + " removed.");
            return true;
        });


        bdv.getBdvHandle().getViewerPanel().state().changeListeners().add(viewerStateChange -> {

            if (bdv.getBdvHandle().getViewerPanel().state().getCurrentSource() == null)
                return;
            if (viewerStateChange.equals(ViewerStateChange.CURRENT_SOURCE_CHANGED))
                for (int i = 0; i < bdv.getSources().size(); i++)
                    if (bdv.getSources().get(i).getSpimSource().equals(bdv.getBdvHandle().getViewerPanel().state().getCurrentSource().getSpimSource())) {
                        System.out.println("Current source position : " + i);
                        sourcesTransformations.setCurrentSource(i);
                        transformationHistoryPanel.setAll(sourcesTransformations.getCurrentTransformations());
                        break;
                    }
        });

        TransformationUpdater updater = (transformation, source) -> {

            bdv.getBdvHandle().getViewerPanel().showMessage("Automatic transformation added ..");
            sourcesTransformations.getCurrentTransformations().add(transformation);
            sourcesTransformations.updateView(bdv);
            transformationHistoryPanel.addTransformation(sourcesTransformations.getCurrentTransformations().getLast());
            bdv.getBdvHandle().getViewerPanel().requestRepaint();
        };

        bdv.getBdvHandle().getManualTransformEditor().manualTransformActiveListeners().add(b -> {
            if (b)
                sourcesTransformations.startManualTransform(bdv);
            if (!b) {
                sourcesTransformations.addManualTransformationFrom(bdv);
                transformationHistoryPanel.addTransformation(sourcesTransformations.getCurrentTransformations().getLast());
            }
        });

        final CardPanel cardPanel = bdv.getBdvHandle().getCardPanel();

        this.controlPanels.add(new TranslationPanel(updater));
        this.controlPanels.add(new ScalingPanel(updater));
        this.controlPanels.add(new RotationPanel(updater));
        this.controlPanels.add(new FlipPanel(updater));
        this.controlPanels.add(new AxisPermutationPanel(updater));
        this.controlPanels.add(new ExportTransformationPanel(e -> {
            AffineTransform3D transform = sourcesTransformations.getCurrentTransformations().get();
            ExportTransformationPanel.save(transform);
        }));

        this.controlPanels.add(transformationHistoryPanel);

        this.controlPanels.forEach(p -> cardPanel.addCard(p.getKey(),
                p.getTitle(),
                p,
                p.isExpend(),
                new Insets(0, 4, 0, 0)));

        BigDataViewerActions actions = new BigDataViewerActions(options.values.getInputTriggerConfig());
        actions.runnableAction(() -> BDVUtils.randomColor(bdv), "Random Color", "R");
        actions.runnableAction(() -> BDVUtils.initBrightness(bdv), "Brightness", "Q");
        actions.runnableAction(() -> undo(bdv), "undo", "ctrl Z");
        actions.runnableAction(() -> redo(bdv), "redo", "ctrl U");
        actions.install(bdv.getBdvHandle().getKeybindings(), "my actions");

        cardPanel.setCardExpanded(BdvDefaultCards.DEFAULT_VIEWERMODES_CARD, false);
        cardPanel.setCardExpanded(BdvDefaultCards.DEFAULT_SOURCES_CARD, false);
        cardPanel.setCardExpanded(BdvDefaultCards.DEFAULT_SOURCEGROUPS_CARD, false);

        bdv.getBdvHandle().getViewerPanel().requestRepaint();
        System.out.println("loaded..");
    }

    public static void main(String[] args) throws IOException {
        new BDVStacking(TEST_DATA.TEST_IMAGE1_PATH, TEST_DATA.TEST_IMAGE2_PATH);
    }

    private void undo(BdvStackSource<T> bdv) {
        if (sourcesTransformations.getCurrentTransformations().canUndo()) {
            bdv.getBdvHandle().getViewerPanel().showMessage("Undo");
            sourcesTransformations.getCurrentTransformations().undo();
            sourcesTransformations.updateView(bdv);
            transformationHistoryPanel.setAll(sourcesTransformations.getCurrentTransformations());
            bdv.getBdvHandle().getViewerPanel().requestRepaint();
        } else {
            bdv.getBdvHandle().getViewerPanel().showMessage("Can't Undo !");
        }
    }

    private void redo(BdvStackSource<T> bdv) {
        if (sourcesTransformations.getCurrentTransformations().canRedo()) {
            bdv.getBdvHandle().getViewerPanel().showMessage("Redo");
            sourcesTransformations.getCurrentTransformations().redo();
            sourcesTransformations.updateView(bdv);
            transformationHistoryPanel.setAll(sourcesTransformations.getCurrentTransformations());
            bdv.getBdvHandle().getViewerPanel().requestRepaint();
        } else {
            bdv.getBdvHandle().getViewerPanel().showMessage("Can't redo !");
        }
    }
}
