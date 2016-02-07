package tests.com.notebook.dsp;

import com.notebook.dsp.BufferChunk;
import com.notebook.dsp.Complex;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class BufferChunkTest {

    private Complex[] mockArray;
    private final int N = 10;

    private static boolean areEqual(BufferChunk a, BufferChunk b) {
        if(a.getCount() != b.getCount()) return false;
        for(int i = 0; i < a.getCount(); ++i) {
            if(a.get(i) != b.get(i)) return false;
        }

        return true;
    }

    @Before
    public void setUp() {
        mockArray = new Complex[N];
        for(int i = 0; i < N; ++i)
            mockArray[i] = new Complex(i,i);
    }

    @Test
    public void testGet() throws Exception {
        int offset = 5;
        int mult = 3;
        int count = 2;
        BufferChunk bf = new BufferChunk(mockArray,offset,mult,count);
        Assert.assertEquals(mockArray[offset + mult], bf.get(1));
    }

    @Test
    public void testConstructorFull() {
        BufferChunk actual = new BufferChunk(mockArray,0,1,N);
        Assert.assertEquals(N,actual.getCount());
        Assert.assertTrue(mockArray == actual.getRawPtr());
        for(int i = 0; i < N; ++i) {
            Assert.assertEquals(mockArray[i],actual.get(i));
        }
    }

    @Test
    public void testConstructorNotFull() {
        BufferChunk actual = new BufferChunk(mockArray,3,4,2);
        Assert.assertEquals(mockArray[3],actual.get(0));
        Assert.assertEquals(mockArray[7],actual.get(1));
    }

    @Test
    public void testGetFull() throws Exception {
        BufferChunk expected = new BufferChunk(mockArray,0,1,N);
        BufferChunk actual = BufferChunk.getFull(mockArray);

        Assert.assertTrue(areEqual(expected,actual));
    }

    @Test
    public void testGetEvensOfFull() throws Exception {
        BufferChunk full = BufferChunk.getFull(mockArray);
        BufferChunk actual = BufferChunk.getEvens(full);

        Assert.assertEquals(N/2,actual.getCount());
        Assert.assertEquals(mockArray[0], actual.get(0));
        Assert.assertEquals(mockArray[2], actual.get(1));
        Assert.assertEquals(mockArray[4], actual.get(2));
        Assert.assertEquals(mockArray[6], actual.get(3));
        Assert.assertEquals(mockArray[8], actual.get(4));
    }

    @Test
    public void testGetOddsOfFull() throws Exception {
        BufferChunk full = BufferChunk.getFull(mockArray);
        BufferChunk actual = BufferChunk.getOdds(full);

        Assert.assertEquals(N/2,actual.getCount());
        Assert.assertEquals(mockArray[1], actual.get(0));
        Assert.assertEquals(mockArray[3], actual.get(1));
        Assert.assertEquals(mockArray[5], actual.get(2));
        Assert.assertEquals(mockArray[7], actual.get(3));
        Assert.assertEquals(mockArray[9], actual.get(4));
    }

    @Test
    public void testGetEvensOfOdds() throws Exception {
        BufferChunk full = BufferChunk.getFull(mockArray);
        BufferChunk odds = BufferChunk.getOdds(full);
        BufferChunk actual = BufferChunk.getEvens(odds);

        Assert.assertEquals(3,actual.getCount());
        Assert.assertEquals(mockArray[1], actual.get(0));
        Assert.assertEquals(mockArray[5], actual.get(1));
        Assert.assertEquals(mockArray[9], actual.get(2));
    }

    @Test
    public void testGetEvensOfEvens() throws Exception {
        BufferChunk full = BufferChunk.getFull(mockArray);
        BufferChunk evens = BufferChunk.getEvens(full);
        BufferChunk actual = BufferChunk.getEvens(evens);

        Assert.assertEquals(3,actual.getCount());
        Assert.assertEquals(mockArray[0], actual.get(0));
        Assert.assertEquals(mockArray[4], actual.get(1));
        Assert.assertEquals(mockArray[8], actual.get(2));
    }

    @Test
    public void testGetOddsOfOdds() throws Exception {
        BufferChunk full = BufferChunk.getFull(mockArray);
        BufferChunk odds = BufferChunk.getOdds(full);
        BufferChunk actual = BufferChunk.getOdds(odds);

        Assert.assertEquals(2,actual.getCount());
        Assert.assertEquals(mockArray[3], actual.get(0));
        Assert.assertEquals(mockArray[7], actual.get(1));
    }
}