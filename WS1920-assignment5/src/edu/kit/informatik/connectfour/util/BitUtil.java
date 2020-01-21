package edu.kit.informatik.connectfour.util;

public class BitUtil {

    // assumes the bit has not been set before
    public static int setBit(int number, int bitIndex) {
        return number + getValueOnBitIndex(bitIndex);
    }

    public static boolean hasBitSet(int number, int bitIndex) {
        return (number % getValueOnBitIndex(bitIndex)) == 0;
    }

    public static int getValueOnBitIndex(int bitIndex) {
        int ret = 1;
        for (int i = 0; i < bitIndex; i++) {
            ret *= 2;
        }
        return ret;
    }
}
