package edu.kit.informatik.torus.model;

import edu.kit.informatik.Terminal;

import java.util.*;

public abstract class Board {

    public static final char EMPTY_BOARD_CHAR = '#';

    public static final int BOARD_SIZE = 6; // assumes quadratic board
    public static final int NEEDED_TO_WIN = 4;

    private Token[][] board = new Token[BOARD_SIZE][BOARD_SIZE];

    public String rowToString(int row) throws RuleException {
        checkRow(row);
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

    public String colToString(int col) throws RuleException {
        checkCol(col);
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

    private void checkRow(int row) throws RuleException {
        if (row < 0 | row > BOARD_SIZE) {
            throw new RuleException("invalid row");
        }
    }

    private void checkCol(int col) throws RuleException {
        if (col < 0 | col > BOARD_SIZE) {
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


    // you better disable parameter hints for this:
    // https://stackoverflow.com/questions/40866202/
    // intellij-shows-method-parameter-hints-on-usage-how-to-disable-it
    boolean winningState() {
        int max = BOARD_SIZE - 1;
        int boundsOffset = getBoundsOffset();
        for (int i = -boundsOffset; i < BOARD_SIZE + boundsOffset; i++) {
            if (winningStateIteration(i, i + 1, 0,
                    -boundsOffset, max + boundsOffset, 1) // cols
                || winningStateIteration(-boundsOffset, max + boundsOffset, 1,
                    i, i + 1, 0) // rows
                || winningStateIteration(i, max + boundsOffset, 1,
                    -boundsOffset, max - i, 1) // diagonal 1, down left half
                || winningStateIteration(-boundsOffset, max - i, 1,
                    i, max + boundsOffset, 1) // diagonal 1, upper right half
                || winningStateIteration(max - i, -boundsOffset, -1,
                    -boundsOffset, max - i, 1) // diagonal 2, top left half
                || winningStateIteration(max + boundsOffset, i, -1,
                    i, max + boundsOffset, 1) // diagonal 2, down right half
            ) {
                return true;
            }
        }
        return false;
    }

    // start, end inclusive
    private boolean winningStateIteration(int rowStartIndex, int rowEndIndex, int rowChange,
                                         int colStartIndex, int colEndIndex, int colChange) {
        /*
        Terminal.printLine(rowStartIndex);
        Terminal.printLine(rowEndIndex);
        Terminal.printLine(rowChange);
        Terminal.printLine(colStartIndex);
        Terminal.printLine(colEndIndex);
        Terminal.printLine(colChange);
        */
        Deque<Token> lastFour = new LinkedList<>();
        int row = rowStartIndex;
        int col = colStartIndex;
        do {
            Position transformed = transform(new Position(row, col));

            // Terminal.printLine(transformed.row());
            // Terminal.printLine(transformed.col());
            lastFour.addFirst(board[transformed.row()][transformed.col()]);
            if (lastFour.size() >= NEEDED_TO_WIN) {
                if (shareAttribute(lastFour)) {
                    return true;
                }
                lastFour.removeLast();
            }
            row += rowChange;
            col += colChange;
            // Terminal.printLine("");
        } while (row - rowChange != rowEndIndex && col - colChange!= colEndIndex);
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

    abstract Position transform(Position pos);

    abstract int getBoundsOffset();
}

