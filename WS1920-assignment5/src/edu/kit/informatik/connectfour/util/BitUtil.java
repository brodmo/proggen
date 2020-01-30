package edu.kit.informatik.connectfour.util;

public final class BitUtil {

    private BitUtil() { }

    public static int setBit(int number, int bitIndex) {
        return number | (1 << bitIndex);
    }

    public static boolean hasBitSet(int number, int bitIndex) {
        return (number & (1 << bitIndex)) != 0;
    }
}
