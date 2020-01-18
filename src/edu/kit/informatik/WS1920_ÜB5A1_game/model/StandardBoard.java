package edu.kit.informatik.WS1920_ÜB5A1_game.model;

public class StandardBoard extends Board {

    @Override
    public Position transform(Position pos) {
        return pos;
    }

    @Override
    int getBoundsOffset() {
        return 0;
    }
}
