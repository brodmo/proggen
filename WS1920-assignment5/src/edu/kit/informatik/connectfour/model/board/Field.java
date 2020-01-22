package edu.kit.informatik.connectfour.model.board;

import edu.kit.informatik.connectfour.model.token.Token;

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

    @Override
    public String toString() {
        if (isEmpty()) {
            return EMPTY_FIELD_STR;
        } else {
            return token.toString();
        }
    }
}
