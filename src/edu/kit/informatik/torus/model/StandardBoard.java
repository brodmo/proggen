package edu.kit.informatik.torus.model;

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
