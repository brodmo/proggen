package edu.kit.informatik.connectfour.model.board;

import edu.kit.informatik.connectfour.model.token.AttributeValue;
import edu.kit.informatik.connectfour.model.RuleException;
import edu.kit.informatik.connectfour.model.token.Token;
import edu.kit.informatik.connectfour.util.StringUtil;

import java.util.*;

public abstract class Board {

    public static final int BOARD_SIZE = 6; // assumes quadratic board
    public static final int NEEDED_TO_WIN = 4;

    private Field[][] board = new Field[BOARD_SIZE][BOARD_SIZE];

    protected Board() {
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

    public boolean winningState() {
        int max = BOARD_SIZE - 1;
        int boundsOffset = getBoundsOffset();
        for (int i = -boundsOffset; i < BOARD_SIZE + boundsOffset; i++) {
            BoardLine[] linesToCheck = {
                    new BoardLine(this::posInBounds, // cols
                            new Position(i, -boundsOffset), 0, 1),
                    new BoardLine(this::posInBounds, // rows
                            new Position(-boundsOffset, i), 1, 0),
                    new BoardLine(this::posInBounds, // diagonal 1, lower left half of the board
                            new Position(i, -boundsOffset), 1, 1),
                    new BoardLine(this::posInBounds, // diagonal 1, upper right half of the board
                            new Position(-boundsOffset, i), 1, 1),
                    new BoardLine(this::posInBounds, // diagonal 2, top left half of the board
                            new Position(max - i, -boundsOffset), -1, 1),
                    new BoardLine(this::posInBounds, // diagonal 2, down right half of the board
                            new Position(max + boundsOffset, i), -1, 1),
            };
            if (winningStateIteration(linesToCheck)) {
                return true;
            }
        }
        return false;
    }

    private boolean winningStateIteration(BoardLine[] linesToCheck) {
        for (BoardLine line: linesToCheck) {
            Deque<Field> lastFour = new LinkedList<>();
            for (Position pos: line) {
                Position transformed = transform(pos);
                lastFour.addFirst(get(transformed));
                if (lastFour.size() >= NEEDED_TO_WIN) {
                    if (shareAttribute(lastFour)) {
                        return true;
                    }
                    lastFour.removeLast();
                }
            }
        }
        return false;
    }


    private boolean shareAttribute(Deque<Field> tokens) {
        Set<AttributeValue> commonAttributes = tokens.pop().getAttributesOfToken();
        for (Field tkn: tokens) {
            commonAttributes.retainAll(tkn.getAttributesOfToken());
        }
        return !commonAttributes.isEmpty();
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

    private boolean coordinateValid(int row) {
        return row >= 0 && row < BOARD_SIZE;
    }

    // different to coordinateValid
    private boolean posInBounds(Position pos) {
        int offset = getBoundsOffset();
        return pos.min() >= -offset && pos.max() < BOARD_SIZE + offset;
    }

    // bare bones, get field at position
    private Field get(Position pos) {
        return board[pos.row()][pos.col()];
    }


    abstract Position transform(Position pos);

    abstract int getBoundsOffset();
}

