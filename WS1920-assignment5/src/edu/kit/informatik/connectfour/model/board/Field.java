package edu.kit.informatik.connectfour.model.board;

import edu.kit.informatik.connectfour.model.token.AttributeValue;
import edu.kit.informatik.connectfour.model.token.Token;

import java.util.HashSet;
import java.util.Set;

/**
 * The type Field.
 * @author The Nipster
 * @version 69.420
 */
class Field {

    /**
     * The constant EMPTY_FIELD_STR.
     */
    public static final String EMPTY_FIELD_STR = "#";

    private Token token = null;

    /**
     * Place.
     *
     * @param token the token
     */
    void place(Token token) {
        this.token = token;
    }

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    boolean isEmpty() {
        return token == null;
    }

    /**
     * Gets attributes of token.
     *
     * @return the attributes of token
     */
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
