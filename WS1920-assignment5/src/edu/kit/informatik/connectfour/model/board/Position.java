package edu.kit.informatik.connectfour.model.board;

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
