package edu.kit.informatik.WS1920_ÜB5A1_game.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Game {

    public static final int NUMBER_OF_TOKENS = 16;

    // is null periodically
    private Token currentToken;
    private int placingPlayer;
    private Board board;
    private Map<Token, Integer> availableTokens;
    private int counter;
    private boolean finished;
    private boolean outOfTokens;

    public Game() {
        reset();
    }

    public void reset() {
        placingPlayer = 2;
        currentToken = null;
        board = null;
        counter = 0;
        availableTokens = new HashMap<>();
        finished = false;
        outOfTokens = false;
        for (int i = 0; i < NUMBER_OF_TOKENS; i++) {
            availableTokens.put(Token.numberToToken(i), 1);
        }
    }

    // returns true on win
    public boolean place(Position pos) throws RuleException {
        checkFinished();
        if (currentToken == null) {
            throw new RuleException("no token has been selected");
        }
        try {
            board.place(pos, currentToken);
        } catch (RuleException e) {
            availableTokens.put(currentToken, availableTokens.get(currentToken) + 1);
            currentToken = null;
            throw e;
        }
        placingPlayer = placingPlayer % 2 + 1;
        currentToken = null;
        Set<Integer> zeroSet = new HashSet<>();
        zeroSet.add(0);
        if (new HashSet<>(availableTokens.values()).equals(zeroSet)) {
            outOfTokens = true;
            finished = true;
        }
        counter++;
        boolean won = board.winningState();
        if (won) {
            finished = true;
        }
        return won;
    }
    public boolean outOfTokens() {
        return outOfTokens;
    }

    public int getPlacingPlayer() {
        return placingPlayer;
    }

    public void take(Token token) throws RuleException {
        checkFinished();
        if (currentToken != null) {
            throw new RuleException("a token has already been selected");
        }
        int available = availableTokens.get(token);
        if (available == 0) {
            throw new RuleException("token is not available (anymore)");
        }
        availableTokens.put(token, available - 1);
        currentToken = token;
    }

    public void setBoardType(BoardType boardType) {
        board = boardType == BoardType.TORUS ? new TorusBoard() : new StandardBoard();
    }

    public boolean boardTypeSet() {
        return board != null;
    }

    public Set<Token> getAvailable() {
        Set<Token> available = new HashSet<>();
        for (int i = 0; i < NUMBER_OF_TOKENS; i++) {
            Token token = Token.numberToToken(i);
            if (availableTokens.get(token) > 0){
                available.add(token);
            }
        }
        return available;
    }

    public String rowToString(int row) throws RuleException {
        return board.rowToString(row);
    }

    public String colToString(int col) throws RuleException {
        return board.colToString(col);
    }


    private void checkFinished() throws RuleException {
        if (finished) {
            throw new RuleException("game is finished");
        }
    }
}
