package edu.kit.informatik.dawn.model;

/**
 * Die möglichen Zustände eines Spielfelds.
 * @author Moritz Brödel
 * @version 1.0
 */
enum FieldState {
    EMPTY("-"),
    PROBE("+"),
    VESTA("V"),
    CERES("C");

    private String string;

    FieldState(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
