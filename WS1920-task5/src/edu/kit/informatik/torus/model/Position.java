package edu.kit.informatik.torus.model;

public class Position {
    private final int row;
    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    int row() {
        return row;
    }

    int col() {
        return col;
    }
}
