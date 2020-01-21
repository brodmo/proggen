package edu.kit.informatik.connectfour.model;

import java.util.*;

public abstract class Board {

    public static final char EMPTY_BOARD_CHAR = '#';

    public static final int BOARD_SIZE = 6; // assumes quadratic board
    public static final int NEEDED_TO_WIN = 4;

    private Token[][] board = new Token[BOARD_SIZE][BOARD_SIZE];

    public static Map<String, Board> getAvailableBoards() {
        Map<String, Board> availableMap = new HashMap<>();
        availableMap.put("standard", new StandardBoard());
        availableMap.put("torus", new TorusBoard());
        return availableMap;
    }

    String rowToString(int row) throws RuleException {
        checkCoordinate(row);
        StringBuilder sb = new StringBuilder();
        for (int col = 0; col < BOARD_SIZE; col++) {
            if (board[row][col] == null) {
                sb.append(EMPTY_BOARD_CHAR);
            } else {
                sb.append(board[row][col].toString());
            }
            sb.append(" "); // this better not count as a magic string
        }
        return sb.substring(0, sb.length() - 1);
    }

    String colToString(int col) throws RuleException {
        checkCoordinate(col);
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < BOARD_SIZE; row++) {
            if (board[row][col] == null) {
                sb.append(EMPTY_BOARD_CHAR);
            } else {
                sb.append(board[row][col].toString());
            }
            sb.append(" "); // this better not count as a magic string
        }
        return sb.substring(0, sb.length() - 1);
    }

    // returns true if placement allowed
    boolean place(Position pos, Token token) throws RuleException {
        Position transformed = transform(pos);
        checkPos(transformed);
        if (get(transformed) != null) {
            return false;
        }
        set(transformed, token);
        return true;
    }

    boolean winningState() {
        int max = BOARD_SIZE - 1;
        int boundsOffset = getBoundsOffset();
        for (int i = -boundsOffset; i < BOARD_SIZE + boundsOffset; i++) {
            BoardLine[] linesToCheck = {
                    // cols
                    new BoardLine(this::posInBounds, new Position(i, -boundsOffset), 0, 1),
                    // rows
                    new BoardLine(this::posInBounds, new Position(-boundsOffset, i), 1, 0),
                    // diagonal 1, lower left half of the board
                    new BoardLine(this::posInBounds, new Position(i, -boundsOffset), 1, 1),
                    // diagonal 1, upper right half of the board
                    new BoardLine(this::posInBounds, new Position(-boundsOffset, i), 1, 1),
                    // diagonal 2, top left half of the board
                    new BoardLine(this::posInBounds, new Position(max - i, -boundsOffset), -1, 1),
                    // diagonal 2, down right half of the board
                    new BoardLine(this::posInBounds, new Position(max + boundsOffset, i), -1, 1),
            };
            if (winningStateIteration(linesToCheck)) {
                return true;
            }
        }
        return false;
    }

    private boolean winningStateIteration(BoardLine[] linesToCheck) {
        for (BoardLine line: linesToCheck) {
            Deque<Token> lastFour = new LinkedList<>();
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


    private boolean shareAttribute(Collection<Token> lastFour) {
        int[] counters = new int[4]; // number of attributes, magic number, sue me
        for (Token token: lastFour) {
            if (token == null) {
                return false;
            }
            if (token.color()) {
                counters[0]++;
            }
            if (token.shape()) {
                counters[1]++;
            }
            if (token.size()) {
                counters[2]++;
            }
            if (token.fullness()) {
                counters[3]++;
            }
        }
        for (int i: counters) {
            if (i == NEEDED_TO_WIN || i == 0) {
                return true;
            }
        }
        return false;
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

    // bare bones, get token at position
    private Token get(Position pos) {
        return board[pos.row()][pos.col()];
    }

    // bare bones, set token at position
    private void set(Position pos, Token token) {
        board[pos.row()][pos.col()] = token;
    }

    abstract Position transform(Position pos);

    abstract int getBoundsOffset();
}

