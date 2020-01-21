package edu.kit.informatik.connectfour.model;

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

    int min() {
        return Math.min(row, col);
    }

    int max() {
        return Math.max(row, col);
    }
}
