package com.notebook.dsp;

/**
 * Standard complex number used for FFT operations
 */
public class Complex {

    private double a = 0;
    private double b = 0;

    public Complex(Complex other) {
        a = other.a;
        b = other.b;
    }

    public Complex(double real, double imaginary) {
        set(real,imaginary);
    }

    public void set(double real, double imaginary) {
        a = real;
        b = imaginary;
    }

    public void set(Complex other) {
        set(other.a,other.b);
    }

    public void plus(Complex other, Complex out) {
        out.set(other.a + a, other.b + b);
    }

    public void minus(Complex other, Complex out) {
        out.set(a - other.a, b - other.b);
    }

    public void times(Complex other, Complex out) {
        out.set(a * other.a - b * other.b, a * other.b + b * other.a);
    }

    public double squaredMag(){
        return a * a + b * b;
    }

    /**
     * Creates a new array of Complex numbers from an array of all real values
     * @param inputs Array of real-only values
     * @return Array of complex values equal to the real array
     */
    public static Complex[] arrayFromDoubles(double[] inputs) {
        Complex[] returnvals = new Complex[inputs.length];
        for(int i = 0; i < returnvals.length; ++i) {
            returnvals[i] = new Complex(inputs[i], 0);
        }
        return returnvals;
    }

    /**
     * Fills the retrunvals array with the real-value inputs.
     * @param inputs Array of real-only values at least of size of count
     * @param returnvals Array of complex numbers equal to the inputs size of
     *                   offset + count
     * @param offset Place to start copying
     * @param count Number of values to copy
     */
    public static void arrayFromDoubles(
            double[] inputs, Complex[] returnvals, int offset, int count) {
        for(int i = 0; i < count; ++i) {
            returnvals[i].set(inputs[i], 0);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Complex other = (Complex) obj;
        return closeEnough(a, other.a) && closeEnough(b, other.b);
    }

    @Override
    public String toString() {
        return String.format("%.2f + %.2fi", a, b);
    }


    private boolean closeEnough(double a, double b) {
        return Math.abs(a - b) < 0.0000001;
    }

}
