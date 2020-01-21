package edu.kit.informatik.connectfour.model;

import static edu.kit.informatik.connectfour.model.Color.BLACK;
import static edu.kit.informatik.connectfour.model.Color.WHITE;
import static edu.kit.informatik.connectfour.model.Fill.HOLLOW;
import static edu.kit.informatik.connectfour.model.Fill.SOLID;
import static edu.kit.informatik.connectfour.model.Shape.ANGULAR;
import static edu.kit.informatik.connectfour.model.Shape.ROTUND;
import static edu.kit.informatik.connectfour.model.Size.LARGE;
import static edu.kit.informatik.connectfour.model.Size.SMALL;

enum Attribute {
    COLOR {
        public int getBitIndex() {
            return 3;
        }

        public AttributeValue getValueOnBitNotSet() {
            return BLACK;
        }

        public AttributeValue getValueOnBitSet() {
            return WHITE;
        }
    },
    FILL {
        public int getBitIndex() {
            return 2;
        }

        public AttributeValue getValueOnBitNotSet() {
            return ANGULAR;
        }

        public AttributeValue getValueOnBitSet() {
            return ROTUND;
        }
    },
    SHAPE {
        public int getBitIndex() {
            return 1;
        }

        public AttributeValue getValueOnBitNotSet() {
            return SMALL;
        }

        public AttributeValue getValueOnBitSet() {
            return LARGE;
        }
    },
    SIZE {
        public int getBitIndex() {
            return 0;
        }

        public AttributeValue getValueOnBitNotSet() {
            return HOLLOW;
        }

        public AttributeValue getValueOnBitSet() {
            return SOLID;
        }
    };

    abstract int getBitIndex();

    abstract AttributeValue getValueOnBitNotSet();

    abstract AttributeValue getValueOnBitSet();
}
