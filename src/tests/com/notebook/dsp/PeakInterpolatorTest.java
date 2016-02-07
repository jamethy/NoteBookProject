package tests.com.notebook.dsp;

import com.notebook.dsp.PeakInterpolator;
import org.junit.Assert;
import org.junit.Test;

public class PeakInterpolatorTest {


    double doublePrecision = 0.0001;

    @Test
    public void testFindMaxIndexInclusiveFirst() throws Exception {
        double[] mock = new double[]{9,3,7,3,3};
        int expected = 2;

        int actual = PeakInterpolator.findMaxIndexInclusive(mock);

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void testFindMaxIndexSecond() throws Exception {
        double[] mock = new double[]{9,9,7,3,3};
        int expected = 1;

        int actual = PeakInterpolator.findMaxIndexInclusive(mock);

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void testFindMaxIndexFirstPenultimate() throws Exception {
        double[] mock = new double[]{9,3,7,9,3};
        int expected = 3;

        int actual = PeakInterpolator.findMaxIndexInclusive(mock);

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void testFindMaxIndexLast() throws Exception {
        double[] mock = new double[]{2,3,7,3,9};
        int expected = 2;

        int actual = PeakInterpolator.findMaxIndexInclusive(mock);

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void testFindMaxIndexMid() throws Exception {
        double[] mock = new double[]{2,3,7,3,3};
        int expected = 2;

        int actual = PeakInterpolator.findMaxIndexInclusive(mock);

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void testBetweenXsExact() throws Exception {
        double[] mockXs = new double[]{1,2,3,4};
        double mockIndex = 1.0;
        double expected = 2;

        double actual = PeakInterpolator.linearInterpolate(mockXs,mockIndex);

        Assert.assertEquals(expected,actual, doublePrecision);
    }

    @Test
    public void testBetweenXsZero() throws Exception {
        double[] mockXs = new double[]{1,2,3,4};
        double mockIndex = 0.0;
        double expected = 1;

        double actual = PeakInterpolator.linearInterpolate(mockXs,mockIndex);

        Assert.assertEquals(expected,actual, doublePrecision);
    }

    @Test
    public void testBetweenXsLast() throws Exception {
        double[] mockXs = new double[]{1,2,3,4};
        double mockIndex = 3.0;
        double expected = 4;

        double actual = PeakInterpolator.linearInterpolate(mockXs,mockIndex);

        Assert.assertEquals(expected,actual, doublePrecision);
    }

    @Test
    public void testBetweenXsMid() throws Exception {
        double[] mockXs = new double[]{1,2,3,4};
        double mockIndex = 1.5;
        double expected = 2.5;

        double actual = PeakInterpolator.linearInterpolate(mockXs,mockIndex);

        Assert.assertEquals(expected,actual, doublePrecision);
    }

    @Test
    public void testBetweenXsNeg() throws Exception {
        double[] mockXs = new double[]{1,2,3,4};
        double mockIndex = -1;
        double expected = 1;

        double actual = PeakInterpolator.linearInterpolate(mockXs,mockIndex);

        Assert.assertEquals(expected,actual, doublePrecision);
    }

    @Test
    public void testBetweenXsOver() throws Exception {
        double[] mockXs = new double[]{1,2,3,4};
        double mockIndex = 6;
        double expected = 4;

        double actual = PeakInterpolator.linearInterpolate(mockXs,mockIndex);

        Assert.assertEquals(expected,actual, doublePrecision);
    }

    @Test
    public void testQuadraticSymmetric() throws Exception {
        double[] mockYs = new double[]{1,3,1};
        double expected = 1;

        double actual = PeakInterpolator.quadratic(mockYs);

        Assert.assertEquals(expected,actual, doublePrecision);
    }

    @Test
    public void testQuadraticMidBetween() throws Exception {
        double[] mockYs = new double[]{-1.25, 0.75, 0.75, -1.25};
        double expected = 1.5;

        double actual = PeakInterpolator.quadratic(mockYs);

        Assert.assertEquals(expected,actual, doublePrecision);
    }
}