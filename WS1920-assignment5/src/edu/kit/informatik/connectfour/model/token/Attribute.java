package edu.kit.informatik.connectfour.model.token;

import static edu.kit.informatik.connectfour.model.token.Color.BLACK;
import static edu.kit.informatik.connectfour.model.token.Color.WHITE;
import static edu.kit.informatik.connectfour.model.token.Fill.HOLLOW;
import static edu.kit.informatik.connectfour.model.token.Fill.SOLID;
import static edu.kit.informatik.connectfour.model.token.Shape.ANGULAR;
import static edu.kit.informatik.connectfour.model.token.Shape.ROTUND;
import static edu.kit.informatik.connectfour.model.token.Size.LARGE;
import static edu.kit.informatik.connectfour.model.token.Size.SMALL;

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
