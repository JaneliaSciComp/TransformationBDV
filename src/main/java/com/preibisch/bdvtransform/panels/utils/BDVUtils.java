package com.preibisch.bdvtransform.panels.utils;

import bdv.tools.brightness.MinMaxGroup;
import bdv.tools.brightness.SetupAssignments;
import bdv.util.BdvStackSource;
import bdv.viewer.Source;
import bdv.viewer.state.ViewerState;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.histogram.DiscreteFrequencyDistribution;
import net.imglib2.histogram.Histogram1d;
import net.imglib2.histogram.Real1dBinMapper;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

/**
 * Code copied from BigStitcher
 * net.preibisch.mvrecon.fiji.spimdata.explorer.popup.BDVPopup
 **/
public class BDVUtils {
    /**
     * set BDV brightness by sampling the mid z plane (and 1/4 and 3/4 if z is large enough )
     * of the currently selected source (typically the first source) and getting quantiles from intensity histogram
     * (slightly modified version of InitializeViewerState.initBrightness)
     *
     * @param cumulativeMinCutoff - quantile of min
     * @param cumulativeMaxCutoff - quantile of max
     * @param state               - Bdv's ViewerSate
     * @param setupAssignments    - Bdv's View assignments
     * @param <T>                 - type extending RealType
     */
    public static <T extends RealType<T>> void initBrightness(final double cumulativeMinCutoff, final double cumulativeMaxCutoff, final ViewerState state, final SetupAssignments setupAssignments) {
        for (int current = 0; current < state.getSources().size(); current++) {
            final Source<?> source = state.getSources().get(current).getSpimSource();
            final int timepoint = state.getCurrentTimepoint();
            if (!source.isPresent(timepoint))
                return;
            if (!RealType.class.isInstance(source.getType()))
                return;
            @SuppressWarnings("unchecked") final RandomAccessibleInterval<T> img = (RandomAccessibleInterval<T>) source.getSource(timepoint, source.getNumMipmapLevels() - 1);
            final long z = (img.min(2) + img.max(2) + 1) / 2;

            final int numBins = 6535;
            final Histogram1d<T> histogram = new Histogram1d<T>(Views.iterable(Views.hyperSlice(img, 2, z)), new Real1dBinMapper<T>(0, 65535, numBins, false));

            // sample some more planes if we have enough
            if ((img.max(2) + 1 - img.min(2)) > 4) {
                final long z14 = (img.min(2) + img.max(2) + 1) / 4;
                final long z34 = (img.min(2) + img.max(2) + 1) / 4 * 3;
                histogram.addData(Views.iterable(Views.hyperSlice(img, 2, z14)));
                histogram.addData(Views.iterable(Views.hyperSlice(img, 2, z34)));
            }

            final DiscreteFrequencyDistribution dfd = histogram.dfd();
            final long[] bin = new long[]{0};
            double cumulative = 0;
            int i = 0;
            for (; i < numBins && cumulative < cumulativeMinCutoff; ++i) {
                bin[0] = i;
                cumulative += dfd.relativeFrequency(bin);
            }
            final int min = i * 65535 / numBins;
            for (; i < numBins && cumulative < cumulativeMaxCutoff; ++i) {
                bin[0] = i;
                cumulative += dfd.relativeFrequency(bin);
            }
            final int max = i * 65535 / numBins;
            final MinMaxGroup minmax = setupAssignments.getMinMaxGroups().get(current);
            minmax.getMinBoundedValue().setCurrentValue(min);
            minmax.getMaxBoundedValue().setCurrentValue(max);
        }
    }

    public static <T extends NumericType<T> & NativeType<T>> void initBrightness(BdvStackSource<T> bdv) {
        BDVUtils.initBrightness(0.001, 0.999, bdv.getBdvHandle().getViewerPanel().getState(), bdv.getBdvHandle().getSetupAssignments());
    }
}
