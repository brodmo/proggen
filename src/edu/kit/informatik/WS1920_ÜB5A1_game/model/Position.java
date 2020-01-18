package edu.kit.informatik.WS1920_ÜB5A1_game.model;

import static edu.kit.informatik.WS1920_ÜB5A1_game.model.Board.BOARD_ROWS;
import static edu.kit.informatik.WS1920_ÜB5A1_game.model.Board.BOARD_COLS;

public class Position {
    private final int row;
    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int row() {
        return row;
    }

    public int col() {
        return col;
    }
}
