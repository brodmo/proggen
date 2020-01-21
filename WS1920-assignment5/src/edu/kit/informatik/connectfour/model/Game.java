package edu.kit.informatik.connectfour.model;

import edu.kit.informatik.connectfour.util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

public class Game {

    public static final int NUMBER_OF_TOKENS = 16;

    // is null periodically
    private Token currentToken; // todo remove null token, introduce NullToken
    private int playerWhoPlaced;
    private Board board;
    private Map<Token, Integer> availableTokens;
    private int counter;
    private boolean finished;
    private boolean outOfTokens;

    public Game() {
        reset();
    }

    public void reset() {
        playerWhoPlaced = 1;
        currentToken = null;
        board = null;
        counter = -1;
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
        if (!board.place(pos, currentToken)) {
            availableTokens.put(currentToken, availableTokens.get(currentToken) + 1);
            currentToken = null;
            throw new RuleException("cannot place token on non-empty field");
        }
        playerWhoPlaced = playerWhoPlaced % 2 + 1;
        currentToken = null;
        if (noMoreTokens()) {
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

    private boolean noMoreTokens() {
        for (int i: availableTokens.values()) {
            if (i != 0) {
                return false;
            }
        }
        return true;
    }

    public int getCounter() {
        return counter;
    }

    public boolean outOfTokens() {
        return outOfTokens;
    }

    public int getPlayerWhoPlaced() {
        return playerWhoPlaced;
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

    public void setBoard(Board board) {
        this.board = board;
    }

    public boolean boardTypeSet() {
        return board != null;
    }

    public String getAvailable() {
        Collection<String> tokenStrings = getAvailableIntern()
                .stream()
                .map(Token::toString)
                .collect(Collectors.toList());
        return StringUtil.join(tokenStrings," ");
    }

    private Set<Token> getAvailableIntern() {
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
