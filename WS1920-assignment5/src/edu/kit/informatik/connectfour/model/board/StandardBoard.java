package edu.kit.informatik.connectfour.model.board;

/**
 * The type Standard board.
 * @author The Nipster
 * @version 69.420
 */
class StandardBoard extends Board {

    @Override
    Position transform(Position pos) {
        return pos;
    }

    @Override
    int getBoundsOffset() {
        return 0;
    }
}
