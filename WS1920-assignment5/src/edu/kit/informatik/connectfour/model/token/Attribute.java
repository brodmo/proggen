package edu.kit.informatik.connectfour.model.token;

enum Attribute {
    COLOR {
        int getBitIndex() {
            return 3;
        }

        AttributeValue getValueOnBitNotSet() {
            return AttributeValue.Color.BLACK;
        }

        AttributeValue getValueOnBitSet() {
            return AttributeValue.Color.WHITE;
        }
    },
    SHAPE {
        int getBitIndex() {
            return 2;
        }

        AttributeValue getValueOnBitNotSet() {
            return AttributeValue.Shape.ANGULAR;
        }

        AttributeValue getValueOnBitSet() {
            return AttributeValue.Shape.ROTUND;
        }
    },
    SIZE {
        int getBitIndex() {
            return 1;
        }

        AttributeValue getValueOnBitNotSet() {
            return AttributeValue.Size.SMALL;
        }

        AttributeValue getValueOnBitSet() {
            return AttributeValue.Size.LARGE;
        }
    },
    FILL {
        int getBitIndex() {
            return 0;
        }

        AttributeValue getValueOnBitNotSet() {
            return AttributeValue.Fill.HOLLOW;
        }

        AttributeValue getValueOnBitSet() {
            return AttributeValue.Fill.SOLID;
        }
    };

    abstract int getBitIndex();

    abstract AttributeValue getValueOnBitNotSet();

    abstract AttributeValue getValueOnBitSet();
}
