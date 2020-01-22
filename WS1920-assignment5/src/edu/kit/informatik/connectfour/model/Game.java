package edu.kit.informatik.connectfour.model;

import edu.kit.informatik.connectfour.model.board.Board;
import edu.kit.informatik.connectfour.model.board.Position;
import edu.kit.informatik.connectfour.model.token.Token;
import edu.kit.informatik.connectfour.util.StringUtil;

import java.util.*;

public class Game {

    public static final int NUMBER_OF_TOKENS = 16;

    // is null periodically
    private Token currentToken;
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
            availableTokens.put(new Token(i), 1);
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
        Collection<String> tokenStrings = new HashSet<>();
        for (Token tkn: getAvailableIntern()) {
            tokenStrings.add(tkn.toString());
        }
        return StringUtil.join(tokenStrings);
    }

    private Set<Token> getAvailableIntern() {
        Set<Token> available = new HashSet<>();
        for (int i = 0; i < NUMBER_OF_TOKENS; i++) {
            Token token = new Token(i);
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
