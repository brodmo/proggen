package edu.kit.informatik.WS1920_ÜB5A1_game.model;

public class TorusBoard extends Board {

    @Override
    public Position transform(Position pos) {
        int row = pos.row() >= 0 ? pos.row() % BOARD_ROWS : BOARD_ROWS - pos.row() % BOARD_ROWS;
        int col = pos.col() >= 0 ? pos.col() % BOARD_COLS : BOARD_COLS - pos.col() % BOARD_COLS;
        return new Position(row, col);
    }

    @Override
    int getBoundsOffset() {
        return NEEDED_TO_WIN - 1;
    }
}
