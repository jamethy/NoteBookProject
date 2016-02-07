package com.notebook.dsp;

/**
 * Points to sections of a buffer.
 * Allows for multiple pointers to the same buffer.
 * Allows for pointing to every nth element (using arrayMultiplier)
 */
public class BufferChunk {

    private Complex[] arrayPtr;
    private int offset;
    private int mult;
    private int count;

    public Complex[] getRawPtr() { return arrayPtr; }
    public int getOffset() { return offset; }
    public int getMultiplier() { return mult; }
    public int getCount() { return count; }

    public BufferChunk(Complex[] theArray,
                       int arrayOffset,
                       int arrayMultiplier,
                       int arrayCount) {
        arrayPtr = theArray;
        offset = arrayOffset;
        mult = arrayMultiplier;
        count = arrayCount;
    }


    /**
     * Returns a BufferChunk pointer to the entire buffer
     * @param buff Buffer to point to
     * @return Pointer to the buffer
     */
    public static BufferChunk getFull(Complex[] buff) {
        return new BufferChunk(buff, 0, 1, buff.length);
    }

    /**
     * Returns a pointer to every 2nth element of the input
     * @param b Buffer chunk to point to
     * @return Pointer to every other element of the input
     */
    public static BufferChunk getEvens(BufferChunk b) {
        return new BufferChunk(
                b.getRawPtr(),
                b.getOffset(),
                b.getMultiplier()*2,
                (b.getCount() + 1) / 2
        );
    }

    /**
     * Returns a pointer to every (2n+1)th element of the input
     * @param b Buffer chunk to point to
     * @return Point to every other element of the input
     */
    public static BufferChunk getOdds(BufferChunk b) {
        return new BufferChunk(
                b.getRawPtr(),
                b.getOffset()+b.getMultiplier(),
                b.getMultiplier()*2,
                b.getCount() /2
        );
    }

    /**
     * Returns a pointer to ith element of the array
     * i times the multiplier plus the offset in the origina
     * @param i Element of the BufferChunk to return
     * @return A pointer to the ith element
     */
    public Complex get(int i) {
        return arrayPtr[offset + i*mult];
    }
}
