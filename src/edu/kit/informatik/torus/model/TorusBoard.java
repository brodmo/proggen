package edu.kit.informatik.torus.model;

public class TorusBoard extends Board {

    @Override
    public Position transform(Position pos) {
        return new Position(transform(pos.row()), transform(pos.col()));
    }

    private int transform(int i) {
        return i >= 0 ? i % BOARD_SIZE : BOARD_SIZE - Math.abs(i) % BOARD_SIZE;
    }

    @Override
    int getBoundsOffset() {
        return NEEDED_TO_WIN - 1;
    }
}
