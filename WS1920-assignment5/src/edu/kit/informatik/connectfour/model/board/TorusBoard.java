package edu.kit.informatik.connectfour.model.board;

class TorusBoard extends Board {

    @Override
    Position transform(Position pos) {
        return new Position(transform(pos.row()), transform(pos.col()));
    }

    private int transform(int i) {
        if (i >= 0) {
            return i % BOARD_SIZE;
        } else {
            return BOARD_SIZE - Math.abs(i) % BOARD_SIZE;
        }
    }

    @Override
    int getBoundsOffset() {
        return NEEDED_TO_WIN / 2;
    }
}
