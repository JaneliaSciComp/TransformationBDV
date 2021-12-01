package com.preibisch.bdvtransform.panels.utils;

public class MathUtils {

    private static double ROUND = 100000.0d;
    static double th = 1E-20;

    public static double round(double x) {
        return (double) Math.round(x * ROUND) / ROUND;
    }

    public static double[] round(double[] array) {
        for (int i = 0; i < array.length; i++)
            array[i] = round(array[i]);
        return array;
    }
}
