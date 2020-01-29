package edu.kit.informatik.connectfour.model.token;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static edu.kit.informatik.connectfour.model.token.AttributeValue.attributesToNumber;
import static edu.kit.informatik.connectfour.model.token.AttributeValue.getAttributes;

public class Token {

    private Set<AttributeValue> attributes;

    public Token(int number) {
        this.attributes = getAttributes(number);
    }

    public Set<AttributeValue> attributes() {
        return Collections.unmodifiableSet(attributes);
    }

    @Override
    public String toString() {
        return Integer.toString(attributesToNumber(attributes));
    }

    public int toInt() {
        return attributesToNumber(attributes);
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
