package com.notebook.dsp;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by james on 1/16/16.
 */
public class BufferedFFT {

    /**
     * Since it is recursive and acts on the previous layer's result, BufferedFFT requires two buffers to store results
     * in. Each buffer is used in every other layer with the layer having a single value always being the odd buffer.
     * @return The buffer for the current layer
     */
    private Complex[] getBuffer(){
        if(useOddBuffer) return oddBuffer;
        else return evenBuffer;
    }

    private boolean useOddBuffer = true;
    private final Complex[] oddBuffer;
    private final Complex[] evenBuffer;

    /** Size of the initial input */
    private final int bufferSize;
    private final Complex[] inputBuffer;

    /**
     * Constructor that initializes all the buffers to size N
     * @param N Maximum size of expected input
     */
    public BufferedFFT(int N) {
        bufferSize = N;
        oddBuffer = new Complex[N];
        evenBuffer = new Complex[N];
        inputBuffer = new Complex[N];
        for(int i = 0; i < N; ++i){
            oddBuffer[i] = new Complex(0,0);
            evenBuffer[i] = new Complex(0,0);
            inputBuffer[i] = new Complex(0,0);
        }
    }

    /**
     * Calculates the FFT of the input.
     *
     * Copies n elements of the byte array input into the buffer and then starts a task that calculates the FFT of that
     * chunk, and returns the future of that task.
     * @param input Array of at least size offset+n to perform the FFT on
     * @param n Number of elements of input to perform FFT on
     * @param offset Place in input to start copying values from
     * @param output Output buffer to copy the results to.
     * @param executor Thread pool to run the FFT calculation on
     * @return A future pointing to the inputted output buffer storing the result of FFT
     * @throws NullPointerException If n is greater than the bufferSize of this object
     * @throws InterruptedException If the FFT calculation is interrupted
     */
    public Future<Complex[]> callableFFT(Complex[] input, int n, int offset, Complex[] output, ExecutorService executor)
            throws NullPointerException, InterruptedException {
        if(n > bufferSize) throw new NullPointerException("Incorrect size");
        for(int i = 0; i < n; ++i) {
            inputBuffer[i].set(input[i+offset]);
        }

        Callable<Complex[]> task = () -> {
            BufferChunk res = fft(new BufferChunk(inputBuffer, 0, 1, n));

            for (int i = 0; i < res.getCount(); ++i) {
                output[i].set(res.get(i));
            }
            return output;
        };

        return executor.submit(task);
    }

    /**
     * Calculates the FFT of the input.
     *
     * Copies n elements of the byte array input into the buffer and then starts a task that calculates the FFT of that
     * chunk, and returns the future of that task.
     * This version does instantiate a new n-size array of Complex variables on the heap for the return val.
     * @param input Array of at least size offset+n to perform the FFT on
     * @param n Number of elements of input to perform FFT on
     * @param offset Place in input to start copying values from
     * @param executor Thread pool to run the FFT calculation on
     * @return A future pointing to the inputted output buffer storing the result of FFT
     * @throws NullPointerException If n is greater than the bufferSize of this object
     * @throws InterruptedException If the FFT calculation is interrupted
     */
    public Future<Complex[]> callableFFT(byte[] input, int n, int offset, ExecutorService executor)
            throws NullPointerException, InterruptedException {
        if(n > bufferSize) throw new NullPointerException("Incorrect size");
        for(int i = 0; i < n; ++i) {
            inputBuffer[i].set(input[i+offset], 0);
        }
        Callable<Complex[]> task = () -> {
            BufferChunk res = fft(new BufferChunk(inputBuffer, 0, 1, n));

            Complex[] ret = new Complex[res.getCount()];
            for (int i = 0; i < res.getCount(); ++i) {
                ret[i] = new Complex(res.get(i));
            }
            return ret;
        };
        return executor.submit(task);
    }

    /**
     * Calculates the FFT of the input.
     *
     * Copies n elements of the byte array input into the buffer and then starts a task that calculates the FFT of that
     * chunk, and returns the future of that task.
     * @param input Array of at least size offset+n to perform the FFT on
     * @param n Number of elements of input to perform FFT on
     * @param offset Place in input to start copying values from
     * @param output Output buffer to copy the results to.
     * @param executor Thread pool to run the FFT calculation on
     * @return A future pointing to the inputted output buffer storing the result of FFT
     * @throws NullPointerException If n is greater than the bufferSize of this object
     * @throws InterruptedException If the FFT calculation is interrupted
     */
    public Future<Complex[]> callableFFT(byte[] input, int n, int offset, Complex[] output, ExecutorService executor)
            throws NullPointerException, InterruptedException {
        if(n > bufferSize) throw new NullPointerException("Incorrect size");
        for(int i = 0; i < n; ++i) {
            inputBuffer[i].set(input[i+offset], 0);
        }
        Callable<Complex[]> task = () -> {
            BufferChunk res = fft(new BufferChunk(inputBuffer, 0, 1, n));

            for (int i = 0; i < res.getCount(); ++i) {
                output[i].set(res.get(i));
            }
            return output;
        };
        return executor.submit(task);
    }

    /**
     * Calculates the FFT of the input.
     *
     * Copies n elements of the byte array input into the buffer and calculates the FFT of that chunk.
     * This version does instantiate a new n-size array of Complex variables on the heap for the return val.
     * @param input Array of at least size offset+n to perform the FFT on
     * @param n Number of elements of input to perform FFT on
     * @param offset Place in input to start copying values from
     * @return Result of FFT
     * @throws Exception If n is greater than the bufferSize of this object
     */
    public synchronized Complex[] calculateFFT(byte[] input, int n, int offset) throws Exception {
        if(n > bufferSize) throw new Exception("Incorrect size");
        for(int i = 0; i < n; ++i) {
            inputBuffer[i].set(input[i+offset], 0);
        }
        BufferChunk res = fft(new BufferChunk(inputBuffer, 0, 1, n));

        Complex[] ret = new Complex[res.getCount()];
        for(int i = 0; i < res.getCount(); ++i){
            ret[i] = new Complex(res.get(i));
        }
        return ret;
    }

    /**
     * Calculates the FFT of the input.
     *
     * Copies n elements of the byte array input into the buffer, calculates the FFT of that chunk, and copies the
     * results to output.
     * This version does not instantiate any buffers on the heap.
     * @param input Array of at least size offset+n to perform the FFT on
     * @param n Number of elements of input to perform FFT on
     * @param offset Place in input to start copying values from
     * @param output Array of at least size n to copy the results to
     * @return Result of FFT
     * @throws Exception If n is greater than the bufferSize of this object
     */
    public void calculateFFT(byte[] input, int n, int offset, Complex[] output) throws Exception {
        if(n > bufferSize) throw new Exception("Incorrect size");
        for(int i = 0; i < n; ++i) {
            inputBuffer[i].set(input[offset+i], 0);
        }
        BufferChunk res = fft(new BufferChunk(
                inputBuffer,
                0,1,n
        ));

        for(int i = 0; i < res.getCount(); ++i){
            output[i].set(res.get(i));
        }
    }

    /**
     * Calculates the FFT of the input
     *
     * Copies the entire array to the input buffer, calculates the FFT of it, and creates and returns the output
     * This version instantiates the input-sized output buffer of Complex variables on the heap.
     * @param input Array to perform the FFT on
     * @return Result of the FFT
     * @throws Exception If size of input is greater than the bufferSize of this object
     */
    public Complex[] calculateFFT(Complex[] input) throws Exception {

        Complex[] output = new Complex[input.length];

        for(int i = 0; i < input.length; ++i) {
            inputBuffer[i].set(input[i]);
            output[i] = new Complex(0,0);
        }

        calculateFFT(input, output);

        return output;
    }

    /**
     * Calculates the FFT of the input
     *
     * Copies the entire array to the input buffer, calculates the FFT of it, and copies it to the output
     * This version instantiates nothing on the heap!
     * @param input Array to perform the FFT on
     * @param output Array to copie the result of the FFT to
     * @throws Exception If size of input is greater than the bufferSize of this object
     */
    public void calculateFFT(Complex[] input, Complex[] output) throws Exception {
        if(input.length > bufferSize) throw new Exception("Incorrect size");

        for(int i = 0; i < input.length; ++i) {
            inputBuffer[i].set(input[i]);
        }
        BufferChunk res = fft(BufferChunk.getFull(inputBuffer));

        for(int i = 0; i < res.getCount(); ++i){
            output[i].set(res.get(i));
        }
    }

    private final Complex wk = new Complex(0,0);
    private final Complex wr = new Complex(0,0);

    private BufferChunk fft(BufferChunk chunk) {

        final int N = chunk.getCount();

        if(N == 1) {
            useOddBuffer = true;
            BufferChunk b = new BufferChunk(
                    getBuffer(),
                    chunk.getOffset(),
                    chunk.getMultiplier(),
                    chunk.getCount()
            );
            b.get(0).set(chunk.get(0));
            return b;
        }

        BufferChunk even = fft(BufferChunk.getEvens(chunk));
        BufferChunk odd = fft(BufferChunk.getOdds(chunk));

        useOddBuffer = !useOddBuffer;

        // combine
        BufferChunk ret = new BufferChunk(
                getBuffer(),
                chunk.getOffset(),
                chunk.getMultiplier(),
                chunk.getCount()
        );

        for (int k = 0; k < N / 2; k++) {
            double kth = -2 * k * Math.PI / N;
            wk.set(Math.cos(kth), Math.sin(kth));
            wk.times(odd.get(k), wr);
            even.get(k).plus(wr, ret.get(k));
            even.get(k).minus(wr, ret.get(k+N/2));
        }
        return ret;
    }
}
