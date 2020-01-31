package edu.kit.informatik.connectfour.model.token;

import edu.kit.informatik.connectfour.util.BitUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * The interface Attribute value.
 * @author The Nipster
 * @version 69.420
 */
public interface AttributeValue {

    /**
     * Gets attributes.
     *
     * @param number the number
     * @return the attributes
     */
    static Set<AttributeValue> getAttributes(int number) {
        Set<AttributeValue> attributes = new HashSet<>();
        for (Attribute attr : Attribute.values()) {
            if (BitUtil.hasBitSet(number, attr.getBitIndex())) {
                attributes.add(attr.getValueOnBitSet());
            } else {
                attributes.add(attr.getValueOnBitNotSet());
            }
        }
        return attributes;
    }

    /**
     * Attributes to number int.
     *
     * @param attributes the attributes
     * @return the int
     */
    static int attributesToNumber(Set<AttributeValue> attributes) {
        int number = 0;
        for (Attribute attr : Attribute.values()) {
            if (attributes.contains(attr.getValueOnBitSet())) {
                number = BitUtil.setBit(number, attr.getBitIndex());
            }
        }
        return number;
    }

    /**
     * The enum Color.
     */
    enum Color implements AttributeValue {
        /**
         * Black color.
         */
        BLACK,
        /**
         * White color.
         */
        WHITE;
    }

    /**
     * The enum Shape.
     */
    enum Shape implements AttributeValue {
        /**
         * Angular shape.
         */
        ANGULAR,
        /**
         * Rotund shape.
         */
        ROTUND;
    }

    /**
     * The enum Size.
     */
    enum Size implements AttributeValue {
        /**
         * Small size.
         */
        SMALL,
        /**
         * Large size.
         */
        LARGE;
    }

    /**
     * The enum Fill.
     */
    enum Fill implements AttributeValue {
        /**
         * Hollow fill.
         */
        HOLLOW,
        /**
         * Solid fill.
         */
        SOLID;
    }
}


