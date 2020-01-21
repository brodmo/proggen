package edu.kit.informatik.connectfour.model;


import java.util.Iterator;
import java.util.function.Function;

class BoardLine implements Iterable<Position> {

    private Function<Position, Boolean> posInBounds;
    private Position startPosition;
    private int rowChange;
    private int colChange;

    BoardLine(Function<Position, Boolean> posInBounds,
              Position startPosition, int rowChange, int colChange) {
        this.posInBounds = posInBounds;
        this.startPosition = startPosition;
        this.rowChange = rowChange;
        this.colChange = colChange;
    }

    @Override
    public Iterator<Position> iterator() {
        return new Iterator<Position>() {
            int row = startPosition.row();
            int col = startPosition.col();

            @Override
            public boolean hasNext() {
                return posInBounds.apply(new Position(row, col));
            }

            @Override
            public Position next() {
                Position nextPos = new Position(row, col);
                row += rowChange;
                col += colChange;
                return nextPos;
            }
        };
    }

}
