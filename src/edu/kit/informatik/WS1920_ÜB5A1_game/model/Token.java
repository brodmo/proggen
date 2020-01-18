package edu.kit.informatik.WS1920_ÜB5A1_game.model;

import java.util.Objects;

public class Token {
    // no time for enum lol
    // schwarz = false, eckig = false, klein = false, hohl = false

    private boolean color;
    private boolean shape;
    private boolean size;
    private boolean fullness;

    public Token(boolean color, boolean shape, boolean size, boolean fullness) {
        this.color = color;
        this.shape = shape;
        this.size = size;
        this.fullness = fullness;
    }

    public static Token numberToToken(int number) {
        boolean color = (number / 8) % 2 != 0; // totally not a magic number :')
        boolean shape = (number / 4) % 2 != 0;
        boolean size = (number / 2) % 2 != 0;
        boolean fullness = number  % 2 != 0;
        return new Token(color, shape, size, fullness);
    }

    public boolean color() {
        return color;
    }

    public boolean shape() {
        return shape;
    }

    public boolean size() {
        return size;
    }

    public boolean fullness() {
        return fullness;
    }

    public int toNumber() {
        int number = 0;
        if (color) {
            number += 8;
        }
        if (shape) {
            number += 4;
        }
        if (size) {
            number += 2;
        }
        if (fullness) {
            number += 1;
        }
        return number;
    }

    @Override
    public String toString() {
        return String.valueOf(toNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, shape, size, fullness);
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
        return color == token.color
                && shape == token.shape
                && size == token.size
                && fullness == token.fullness;

    }
}
