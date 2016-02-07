package com.notebook.dsp;

/**
 * Simple low pass filter class with apply methods.
 */
public class LowPassFilter {

    private double mAlpha;

    public LowPassFilter(double timestep, double frequency) {
        double rc = 1.0/(2*Math.PI*frequency);
        mAlpha = timestep / (rc + timestep);
    }

    public void apply(byte[] vals, int N, int offset) {
        for(int i = 1; i < N; ++i) {
            int j = i + offset;
            vals[j] = (byte)(vals[j-1] + mAlpha*(vals[j] - vals[j-1]));
        }
    }

    public void apply(double[] vals, int N, int offset) {
        for(int i = 1; i < N; ++i) {
            int j = i + offset;
            vals[j] = vals[j-1] + mAlpha*(vals[j] - vals[j-1]);
        }
    }

    public void apply(Complex[] vals, int N, int offset) {
        Complex temp = new Complex(0,0);
        for(int i = 1; i < N; ++i) {
            int j = i + offset;
            vals[j].minus(vals[j-1],temp);
            temp.times(mAlpha,temp);
            vals[j-1].plus(temp,vals[j]);
        }
    }
}
