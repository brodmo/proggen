package edu.kit.informatik.WS1920_ÜB5A1_game.model;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Board {

    public static final char EMPTY_BOARD_CHAR = '#';

    public static final int BOARD_ROWS = 6;
    public static final int BOARD_COLS = 6;
    public static final int NEEDED_TO_WIN = 4;

    private Token[][] board = new Token[BOARD_ROWS][BOARD_COLS];

    public String rowToString(int row) throws RuleException {
        checkRow(row);
        StringBuilder sb = new StringBuilder();
        for (int col = 0; col < BOARD_COLS; col++) {
            if (board[row][col] == null) {
                sb.append(EMPTY_BOARD_CHAR);
            } else {
                sb.append(board[row][col].toString());
            }
            sb.append(" "); // this better not count as a magic string
        }
        return sb.substring(0, sb.length() - 1);
    }

    public String colToString(int col) throws RuleException {
        checkCol(col);
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < BOARD_COLS; row++) {
            if (board[row][col] == null) {
                sb.append(EMPTY_BOARD_CHAR);
            } else {
                sb.append(board[row][col].toString());
            }
            sb.append(" "); // this better not count as a magic string
        }
        return sb.substring(0, sb.length() - 1);
    }

    private void checkRow(int row) throws RuleException {
        if (row < 0 | row > BOARD_ROWS) {
            throw new RuleException("invalid row");
        }
    }

    private void checkCol(int col) throws RuleException {
        if (col < 0 | col > BOARD_COLS) {
            throw new RuleException("invalid col");
        }
    }

    private void checkPos(Position pos) throws RuleException {
        checkRow(pos.row());
        checkCol(pos.col());
    }

    void place(Position pos, Token token) throws RuleException {
        Position transformed = transform(pos);
        checkPos(transformed); // a tad hacky, todo think of better solution
        int row = transformed.row();
        int col = transformed.col();
        if (board[row][col] != null) {
            throw new RuleException("cannot place token on non-empty field");
        }
        board[row][col] = token;
    }

    public boolean winningState() {
        int boundsOffset = getBoundsOffset();
        return winningStateIteration(-boundsOffset, -boundsOffset,
                        BOARD_ROWS + boundsOffset + 1, BOARD_COLS + boundsOffset + 1,
                        0, 0)
                || winningStateIteration(-boundsOffset, -boundsOffset,
                        BOARD_ROWS + boundsOffset + 1, BOARD_COLS + boundsOffset + 1,
                        0, 1)
                || winningStateIteration(-boundsOffset, -boundsOffset,
                BOARD_ROWS + boundsOffset + 1, BOARD_COLS + boundsOffset + 1,
                1, 0)
                || winningStateIteration(-boundsOffset, -BOARD_COLS + boundsOffset,
                BOARD_ROWS + boundsOffset + 1, -boundsOffset - 1,
                1, -1);

    }

    public boolean winningStateIteration(int rowStartIndex, int colStartIndex,
                                         int rowEndIndex, int colEndIndex,
                                         int rowChange, int colChange) {
        ArrayList<Token> lastFour = new ArrayList<>();
        int row = rowStartIndex;
        int col = colStartIndex;
        while (row != rowEndIndex && col != colEndIndex) {
            Position transformed = transform(new Position(row, col));
            lastFour.add(board[transformed.row()][transformed.col()]);
            if (shareAttribute(lastFour)) {
                return true;
            }
            row += rowChange;
            col += colChange;
        }
        return false;
    }

    private boolean shareAttribute(Collection<Token> lastFour) {
        if (lastFour.size() < NEEDED_TO_WIN) {
            return false;
        }
        int[] counters = new int[4]; // number of attributes, magic number, sue me
        for (Token token: lastFour) {
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

    abstract Position transform(Position pos);

    abstract int getBoundsOffset();
}

