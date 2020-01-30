package edu.kit.informatik.connectfour.model.board;

class TorusBoard extends Board {

    @Override
    Position transform(Position pos) {
        return new Position(transform(pos.row()), transform(pos.col()));
    }

    private int transform(int coord) {
        return ((coord % BOARD_SIZE) + BOARD_SIZE) % BOARD_SIZE;
    }

    @Override
    int getBoundsOffset() {
        return NEEDED_TO_WIN / 2;
    }
}
