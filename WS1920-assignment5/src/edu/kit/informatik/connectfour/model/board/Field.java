package edu.kit.informatik.connectfour.model.board;

import edu.kit.informatik.connectfour.model.token.AttributeValue;
import edu.kit.informatik.connectfour.model.token.Token;

import java.util.HashSet;
import java.util.Set;

class Field {

    public static final String EMPTY_FIELD_STR = "#";

    private Token token = null;

    void place(Token token) {
        this.token = token;
    }

    boolean isEmpty() {
        return token == null;
    }

    Token token() {
        return token;
    }

    Set<AttributeValue> getAttributesOfToken() {
        if (isEmpty()) {
            return new HashSet<>();
        } else {
            return token.attributes();
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return EMPTY_FIELD_STR;
        } else {
            return token.toString();
        }
    }
}
