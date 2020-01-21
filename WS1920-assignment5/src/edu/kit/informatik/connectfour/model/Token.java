package edu.kit.informatik.connectfour.model;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static edu.kit.informatik.connectfour.model.AttributeValue.attributesToNumber;
import static edu.kit.informatik.connectfour.model.AttributeValue.getAttributes;

public class Token {

    private Set<AttributeValue> attributes;

    public Token(int number) {
        attributes = getAttributes(number);
    }

    private Token(Set<AttributeValue> attributes) {
        this.attributes = attributes;
    }

    Set<AttributeValue> attributes() {
        return Collections.unmodifiableSet(attributes);
    }

    @Override
    public String toString() {
        return Integer.toString(attributesToNumber(attributes));
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
