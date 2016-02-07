package com.notebook.dsp;

/**
 *
 */
public class PeakInterpolator {

    /**
     * Finds the index of the maximum value in the array, excluding the first
     * and last values.
     * @param vals Array of values to search for maximum
     * @return The index of the maximum value
     */
    public static int findMaxIndexInclusive(double[] vals) {
        int maxI = 1;
        for(int i = 2; i < vals.length-1; ++i){
            if(vals[i] > vals[maxI]) maxI = i;
        }
        return maxI;
    }

    /**
     * Linearly interpolates a value between the values of x at floating index x
     * @param x Array of values to interpolate within
     * @param index Floating index to interpolate at
     * @return Interpolated value
     */
    public static double linearInterpolate(double[] x, double index){
        if(index < 0) return x[0];
        else if (index > x.length) return x[x.length-1];

        int lowi = (int)Math.floor(index);
        int upi = (int)Math.ceil(index);

        if(lowi == upi) return x[lowi];

        return x[lowi] + (x[upi]-x[lowi])*(index-lowi);
    }

    /**
     * Finds the peak of the curve using quadratic interpolation
     * @param y Array of values to find the peak of
     * @return Peak value of the y array
     */
    public static double quadratic(double[] y) {
        int m = findMaxIndexInclusive(y);
        double d = (y[m+1]-y[m-1])/(2*(2*y[m]-y[m-1]-y[m+1]));
        double index = m + d;

        if(index < 0) return 0;
        else if(index >= y.length) return y.length-1;
        else return index;
    }

    /**
     * Finds the peak of the curve using barycentric interpolation
     * @param y Array of values to find the peak of
     * @return Peak value of the y array
     */
    public static double barycentric(double[] y) {
        int m = findMaxIndexInclusive(y);
        double d = (y[m+1]-y[m-1])/(y[m-1]+y[m]+y[m+1]);
        return m + d;
    }
}
