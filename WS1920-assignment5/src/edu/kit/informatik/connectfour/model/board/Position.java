package edu.kit.informatik.connectfour.model.board;

/**
 * The type Position.
 * @author The Nipster
 * @version 69.420
 */
public class Position {

    private final int row;
    private final int col;

    /**
     * Instantiates a new Position.
     *
     * @param row the row
     * @param col the col
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Row int.
     *
     * @return the int
     */
    int row() {
        return row;
    }

    /**
     * Col int.
     *
     * @return the int
     */
    int col() {
        return col;
    }
}
