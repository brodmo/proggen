package edu.kit.informatik.connectfour.model.board;

import edu.kit.informatik.connectfour.model.RuleException;
import edu.kit.informatik.connectfour.model.token.AttributeValue;
import edu.kit.informatik.connectfour.model.token.Token;
import edu.kit.informatik.connectfour.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public abstract class Board {

    public static final int BOARD_SIZE = 6; // assumes quadratic board
    public static final int NEEDED_TO_WIN = 4;

    private Field[][] board = new Field[BOARD_SIZE][BOARD_SIZE];

    Board() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col] = new Field();
            }
        }
    }

    public static Map<String, Board> getAvailableBoards() {
        Map<String, Board> availableMap = new HashMap<>();
        availableMap.put("standard", new StandardBoard());
        availableMap.put("torus", new TorusBoard());
        return availableMap;
    }

    // returns true if placement allowed
    public boolean place(Position pos, Token token) throws RuleException {
        Position transformed = transform(pos);
        checkPos(transformed);
        Field fld = get(transformed);
        if (!fld.isEmpty()) {
            return false;
        }
        fld.place(token);
        return true;
    }

    public String rowToString(int row) throws RuleException {
        checkCoordinate(row);
        List<String> rowStrings = new ArrayList<>();
        for (int col = 0; col < BOARD_SIZE; col++) {
            rowStrings.add(board[row][col].toString());
        }
        return StringUtil.join(rowStrings);
    }

    public String colToString(int col) throws RuleException {
        checkCoordinate(col);
        List<String> colStrings = new ArrayList<>();
        for (int row = 0; row < BOARD_SIZE; row++) {
            colStrings.add(board[row][col].toString());
        }
        return StringUtil.join(colStrings);
    }

    public boolean winningState() {
        int boundsOffset = getBoundsOffset();
        for (int i = -boundsOffset; i < BOARD_SIZE + boundsOffset; i++) {
            for (BoardLine line: getLinesToCheck(i)) {
                if (checkLine(line)) {
                    return true;
                }
            }
        }
        return false;
    }

    abstract Position transform(Position pos);

    abstract int getBoundsOffset();

    private Set<BoardLine> getLinesToCheck(int i) {
        int boundsOffset = getBoundsOffset();
        Set<BoardLine> linesToCheck = new HashSet<>(Arrays.asList(
                new BoardLine(new Position(i, -boundsOffset), 0, 1), // cols
                new BoardLine(new Position(-boundsOffset, i), 1, 0), // rows
                // diagonal 1, lower left half of the board
                new BoardLine(new Position(i, -boundsOffset), 1, 1),
                // diagonal 2, top left half of the board
                new BoardLine(new Position(BOARD_SIZE - 1 - i, -boundsOffset), -1, 1)
        ));
        if (i > -boundsOffset) { // so the middle diagonals don't get checked twice
            linesToCheck.addAll(Arrays.asList(
                    // diagonal 1, upper right half of the board
                    new BoardLine(new Position(-boundsOffset, i), 1, 1),
                    // diagonal 2, down right half of the board
                    new BoardLine(new Position(BOARD_SIZE - 1 + boundsOffset, i), -1, 1)
            ));
        }
        return linesToCheck;
    }

    private boolean checkLine(BoardLine line) {
        Queue<Field> lastFields = new LinkedList<>();
        for (Position pos: line) {
            lastFields.add(get(transform(pos)));
            if (lastFields.size() >= NEEDED_TO_WIN) {
                if (shareAttribute(lastFields)) {
                    return true;
                }
                lastFields.remove();
            }
        }
        return false;
    }

    private boolean shareAttribute(Queue<Field> fields) {
        Set<AttributeValue> commonAttributes = new HashSet<>(
                fields.element().getAttributesOfToken());
        // skip first iteration
        for (Field fld: new LinkedList<>(fields).subList(1, fields.size())) {
        /* illegal :sob:
        for (Field fld: fields.stream().skip(1).collect(Collectors.toList())) { */
            commonAttributes.retainAll(fld.getAttributesOfToken());
        }
        return !commonAttributes.isEmpty();
    }

    private Field get(Position pos) {
        return board[pos.row()][pos.col()];
    }

    private boolean coordinateValid(int coord) {
        return coord >= 0 && coord < BOARD_SIZE;
    }

    private void checkCoordinate(int coord) throws RuleException {
        if (!coordinateValid(coord)) {
            throw new RuleException("invalid coordinate(s)");
        }
    }

    private void checkPos(Position pos) throws RuleException {
        checkCoordinate(pos.row());
        checkCoordinate(pos.col());
    }

    private class BoardLine implements Iterable<Position> {

        private final Position startPosition;
        private final int rowChange;
        private final int colChange;

        private BoardLine(Position startPosition, int rowChange, int colChange) {
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
                    int offset = getBoundsOffset();
                    return Math.min(row, col) >= -offset
                            && Math.max(row, col) < BOARD_SIZE + offset;
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
}

