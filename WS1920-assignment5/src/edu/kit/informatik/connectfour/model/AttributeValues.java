package edu.kit.informatik.connectfour.model;

import edu.kit.informatik.connectfour.util.BitUtil;

import java.util.HashSet;
import java.util.Set;

interface AttributeValue {
    static Set<AttributeValue> getAttributes(int number) {
        Set<AttributeValue> attributes = new HashSet<>();
        for (Attribute attr: Attribute.values()) {
            if (BitUtil.hasBitSet(number, attr.getBitIndex())) {
                attributes.add(attr.getValueOnBitSet());
            } else {
                attributes.add(attr.getValueOnBitNotSet());
            }
        }
        return attributes;
    }

    static int attributesToNumber(Set<AttributeValue> attributes) {
        int number = 0;
        for (Attribute attr: Attribute.values()) {
            if (attributes.contains(attr.getValueOnBitSet())) {
                number = BitUtil.setBit(number, attr.getBitIndex());
            }
        }
        return number;
    }
}

enum Color implements AttributeValue {
    BLACK, WHITE;
}

enum Shape implements AttributeValue {
    ANGULAR, ROTUND;
}

enum Size implements AttributeValue {
    SMALL, LARGE;
}

enum Fill implements AttributeValue {
    HOLLOW, SOLID;
}



