package edu.kit.informatik.connectfour.model.token;

/**
 * The enum Attribute.
 * @author The Nipster
 * @version 69.420
 */
enum Attribute {
    /**
     * The Color.
     */
    COLOR {
        @Override
        int getBitIndex() {
            return 3;
        }

        @Override
        AttributeValue getValueOnBitNotSet() {
            return AttributeValue.Color.BLACK;
        }

        @Override
        AttributeValue getValueOnBitSet() {
            return AttributeValue.Color.WHITE;
        }
    },
    /**
     * The Shape.
     */
    SHAPE {
        @Override
        int getBitIndex() {
            return 2;
        }

        @Override
        AttributeValue getValueOnBitNotSet() {
            return AttributeValue.Shape.ANGULAR;
        }

        @Override
        AttributeValue getValueOnBitSet() {
            return AttributeValue.Shape.ROTUND;
        }
    },
    /**
     * The Size.
     */
    SIZE {
        @Override
        int getBitIndex() {
            return 1;
        }

        @Override
        AttributeValue getValueOnBitNotSet() {
            return AttributeValue.Size.SMALL;
        }

        @Override
        AttributeValue getValueOnBitSet() {
            return AttributeValue.Size.LARGE;
        }
    },
    /**
     * The Fill.
     */
    FILL {
        @Override
        int getBitIndex() {
            return 0;
        }

        @Override
        AttributeValue getValueOnBitNotSet() {
            return AttributeValue.Fill.HOLLOW;
        }

        @Override
        AttributeValue getValueOnBitSet() {
            return AttributeValue.Fill.SOLID;
        }
    };

    /**
     * Gets bit index.
     *
     * @return the bit index
     */
    abstract int getBitIndex();

    /**
     * Gets value on bit not set.
     *
     * @return the value on bit not set
     */
    abstract AttributeValue getValueOnBitNotSet();

    /**
     * Gets value on bit set.
     *
     * @return the value on bit set
     */
    abstract AttributeValue getValueOnBitSet();
}
