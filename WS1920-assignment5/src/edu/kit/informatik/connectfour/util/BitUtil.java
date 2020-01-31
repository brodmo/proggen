package edu.kit.informatik.connectfour.util;

/**
 * The type Bit util.
 * @author The Nipster
 * @version 69.420
 */
public final class BitUtil {

    private BitUtil() { }

    /**
     * Sets bit.
     *
     * @param number   the number
     * @param bitIndex the bit index
     * @return the bit
     */
    public static int setBit(int number, int bitIndex) {
        return number | (1 << bitIndex);
    }

    /**
     * Has bit set boolean.
     *
     * @param number   the number
     * @param bitIndex the bit index
     * @return the boolean
     */
    public static boolean hasBitSet(int number, int bitIndex) {
        return (number & (1 << bitIndex)) != 0;
    }
}
