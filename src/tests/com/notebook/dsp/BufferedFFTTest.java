package tests.com.notebook.dsp;

import com.notebook.dsp.BufferedFFT;
import com.notebook.dsp.Complex;
import com.notebook.dsp.BufferChunk;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.*;

public class BufferedFFTTest {

    @Test
    public void testBufferChunkFull() throws Exception {

        Complex[] input = new Complex[4];
        input[0] = new Complex(1,0);
        input[1] = new Complex(8,0);
        input[2] = new Complex(3,0);
        input[3] = new Complex(2,0);

        BufferChunk b = BufferChunk.getFull(input);

        assertEquals(input.length, b.getCount());

        Complex[] actual = new Complex[4];
        for(int i = 0; i < b.getCount(); ++i) {
            actual[i] = new Complex(b.get(i));
        }

        assertTrue(Arrays.equals(input, actual));
    }

    @Test
    public void testBufferChunkEven() throws Exception {

        Complex[] input = new Complex[4];
        input[0] = new Complex(1,0);
        input[1] = new Complex(8,0);
        input[2] = new Complex(3,0);
        input[3] = new Complex(2,0);

        Complex[] expected = new Complex[2];
        expected[0] = new Complex(1,0);
        expected[1] = new Complex(3,0);

        BufferChunk b = BufferChunk.getFull(input);
        BufferChunk even = BufferChunk.getEvens(b);

        assertEquals(expected.length, even.getCount());

        for(int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], even.get(i));
        }
    }

    @Test
    public void testBufferChunkOdd() throws Exception {

        Complex[] input = new Complex[4];
        input[0] = new Complex(1,0);
        input[1] = new Complex(8,0);
        input[2] = new Complex(3,0);
        input[3] = new Complex(2,0);

        Complex[] expected = new Complex[2];
        expected[0] = new Complex(8,0);
        expected[1] = new Complex(2,0);

        BufferChunk b = BufferChunk.getFull(input);
        BufferChunk odd = BufferChunk.getOdds(b);

        assertEquals(expected.length, odd.getCount());

        for(int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], odd.get(i));
        }
    }

    @Test
    public void testBufferChunkSingleEven() throws Exception {

        Complex[] input = new Complex[4];
        input[0] = new Complex(1,0);
        input[1] = new Complex(8,0);
        input[2] = new Complex(3,0);
        input[3] = new Complex(2,0);

        Complex[] expected = new Complex[1];
        expected[0] = new Complex(1,0);

        BufferChunk b = BufferChunk.getFull(input);
        BufferChunk b2 = BufferChunk.getEvens(b);
        BufferChunk b3 = BufferChunk.getEvens(b2);

        assertEquals(expected.length, b3.getCount());

        for(int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], b3.get(i));
        }
    }


    @Test
    public void testBufferChunkSingleOdd() throws Exception {

        Complex[] input = new Complex[4];
        input[0] = new Complex(1,0);
        input[1] = new Complex(8,0);
        input[2] = new Complex(3,0);
        input[3] = new Complex(2,0);

        Complex[] expected = new Complex[1];
        expected[0] = new Complex(3,0);

        BufferChunk b = BufferChunk.getFull(input);
        BufferChunk b1 = BufferChunk.getEvens(b);
        BufferChunk b2 = BufferChunk.getOdds(b1);

        assertEquals(expected.length, b2.getCount());

        for(int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], b2.get(i));
        }
    }


    @Test
    public void testFft_allReal() throws Exception {

        Complex[] input = new Complex[4];
        input[0] = new Complex(1,0);
        input[1] = new Complex(8,0);
        input[2] = new Complex(3,0);
        input[3] = new Complex(2,0);

        Complex[] expected = new Complex[4];
        expected[0] = new Complex(14,0);
        expected[1] = new Complex(-2,-6);
        expected[2] = new Complex(-6,0);
        expected[3] = new Complex(-2,6);

        BufferedFFT bufferedFFT = new BufferedFFT(input.length);
        Complex[] actual = bufferedFFT.calculateFFT(input);

        assertTrue(Arrays.equals(expected, actual));
    }


    @Test
    public void testFft_mix() throws Exception {

        Complex[] input = new Complex[4];
        input[0] = new Complex(1,1);
        input[1] = new Complex(3,0);
        input[2] = new Complex(3,3);
        input[3] = new Complex(6,0);

        Complex[] expected = new Complex[4];
        expected[0] = new Complex(13,4);
        expected[1] = new Complex(-2,1);
        expected[2] = new Complex(-5,4);
        expected[3] = new Complex(-2,-5);

        BufferedFFT bufferedFFT = new BufferedFFT(input.length);
        Complex[] actual = bufferedFFT.calculateFFT(input);

        assertTrue(Arrays.equals(expected, actual));
    }

    @Test
    public void testFFT_fromBytes() throws Exception {

        byte[] input = new byte[]{1,8,3,2};

        Complex[] expected = new Complex[4];
        expected[0] = new Complex(14,0);
        expected[1] = new Complex(-2,-6);
        expected[2] = new Complex(-6,0);
        expected[3] = new Complex(-2,6);

        BufferedFFT bufferedFFT = new BufferedFFT(input.length);
        Complex[] actual = bufferedFFT.calculateFFT(input,input.length,0);

        assertTrue(Arrays.equals(expected, actual));
    }

    @Test
    public void testFFTCallable() throws Exception {

        byte[] input = new byte[]{1,8,3,2};

        Complex[] expected = new Complex[4];
        expected[0] = new Complex(14,0);
        expected[1] = new Complex(-2,-6);
        expected[2] = new Complex(-6,0);
        expected[3] = new Complex(-2,6);

        BufferedFFT bufferedFFT = new BufferedFFT(input.length);

        ExecutorService executor = Executors.newFixedThreadPool(1);

        Future<Complex[]> futureActual = bufferedFFT.callableFFT(input,input.length,0,executor);

        assertTrue(Arrays.equals(expected, futureActual.get()));
    }

    @Test
    public void testFFTCallableBuffered() throws Exception {

        byte[] input = new byte[]{1,8,3,2};

        Complex[] expected = new Complex[4];
        expected[0] = new Complex(14,0);
        expected[1] = new Complex(-2,-6);
        expected[2] = new Complex(-6,0);
        expected[3] = new Complex(-2,6);

        Complex[] outputBuffer = new Complex[4];
        for(int i = 0; i < 4; ++i) outputBuffer[i] = new Complex(0,0);

        BufferedFFT bufferedFFT = new BufferedFFT(input.length);

        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<Complex[]> futureActual = bufferedFFT.callableFFT(input,input.length,0,outputBuffer,executor);

        assertTrue(Arrays.equals(expected, futureActual.get()));
    }

    @Test
    public void testFFTCallableSameBuffer() throws Exception {

        byte[] input = new byte[]{1,8,3,2,1,8,3,2};

        Complex[] expected = new Complex[4]; //for both left and right
        expected[0] = new Complex(14,0);
        expected[1] = new Complex(-2,-6);
        expected[2] = new Complex(-6,0);
        expected[3] = new Complex(-2,6);

        Complex[] outputBuffer1 = new Complex[4];
        Complex[] outputBuffer2 = new Complex[4];
        for(int i = 0; i < 4; ++i) {
            outputBuffer1[i] = new Complex(0,0);
            outputBuffer2[i] = new Complex(0,0);
        }

        BufferedFFT bufferedFFT1 = new BufferedFFT(input.length/2);
        BufferedFFT bufferedFFT2 = new BufferedFFT(input.length/2);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<Complex[]> futureActual1 = bufferedFFT1.callableFFT(input,input.length/2,0,outputBuffer1,executor);
        Future<Complex[]> futureActual2 = bufferedFFT2.callableFFT(input,input.length/2,4,outputBuffer2,executor);

        assertTrue(Arrays.equals(expected, futureActual1.get()));
        assertTrue(Arrays.equals(expected, futureActual2.get()));
    }

    @Test
    public void testFFTCallableComplexBuffer() throws Exception {

        Complex[] input = new Complex[] {
                new Complex(1,0),
                new Complex(8,0),
                new Complex(3,0),
                new Complex(2,0),
                new Complex(1,0),
                new Complex(8,0),
                new Complex(3,0),
                new Complex(2,0)
        };

        Complex[] expected = new Complex[4]; //for both left and right
        expected[0] = new Complex(14,0);
        expected[1] = new Complex(-2,-6);
        expected[2] = new Complex(-6,0);
        expected[3] = new Complex(-2,6);

        Complex[] outputBuffer1 = new Complex[4];
        Complex[] outputBuffer2 = new Complex[4];
        for(int i = 0; i < 4; ++i) {
            outputBuffer1[i] = new Complex(0,0);
            outputBuffer2[i] = new Complex(0,0);
        }

        BufferedFFT bufferedFFT1 = new BufferedFFT(input.length/2);
        BufferedFFT bufferedFFT2 = new BufferedFFT(input.length/2);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<Complex[]> futureActual1 = bufferedFFT1.callableFFT(input,input.length/2,0,outputBuffer1,executor);
        Future<Complex[]> futureActual2 = bufferedFFT2.callableFFT(input,input.length/2,4,outputBuffer2,executor);

        assertTrue(Arrays.equals(expected, futureActual1.get()));
        assertTrue(Arrays.equals(expected, futureActual2.get()));
    }
}