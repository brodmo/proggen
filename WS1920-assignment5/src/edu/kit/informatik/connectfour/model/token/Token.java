package edu.kit.informatik.connectfour.model.token;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * The type Token.
 * @author The Nipster
 * @version 69.420
 */
public class Token {

    private Set<AttributeValue> attributes;

    /**
     * Instantiates a new Token.
     *
     * @param number the number
     */
    public Token(int number) {
        this.attributes = AttributeValue.getAttributes(number);
    }

    /**
     * Attributes set.
     *
     * @return the set
     */
    public Set<AttributeValue> attributes() {
        return Collections.unmodifiableSet(attributes);
    }

    @Override
    public String toString() {
        return Integer.toString(AttributeValue.attributesToNumber(attributes));
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Token token = (Token) obj;
        return attributes.equals(token.attributes);
    }
}
