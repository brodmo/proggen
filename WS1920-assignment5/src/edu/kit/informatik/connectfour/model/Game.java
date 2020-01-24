package edu.kit.informatik.connectfour.model;

import edu.kit.informatik.connectfour.model.board.Board;
import edu.kit.informatik.connectfour.model.board.Position;
import edu.kit.informatik.connectfour.model.token.Token;
import edu.kit.informatik.connectfour.util.StringUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Game {

    public static final int NUMBER_OF_TOKENS = 16;

    private Board board;
    private Map<Token, Integer> availableTokens;
    private Token selectedToken; // is null when no token selected
    private int playerWhoPlaced;
    private int counter;
    private boolean finished;
    private boolean outOfTokens;

    public Game() {
        reset();
    }

    public void reset() {
        // your responsibility if board is not set and you call a method that needs it ¯\_(ツ)_/¯
        board = null;
        takeSelectedToken();
        playerWhoPlaced = 1;
        counter = -1;
        finished = false;
        outOfTokens = false;
        availableTokens = new HashMap<>();
        for (int i = 0; i < NUMBER_OF_TOKENS; i++) {
            availableTokens.put(new Token(i), 1);
        }
    }

    public int getPlayerWhoPlaced() {
        return playerWhoPlaced;
    }

    public int getCounter() {
        return counter;
    }

    public boolean outOfTokens() {
        return outOfTokens;
    }

    public boolean boardSet() {
        return board != null;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void take(Token token) throws RuleException {
        checkFinished();
        if (tokenSelected()) {
            throw new RuleException("a token has already been selected");
        }
        int available = availableTokens.get(token);
        if (available <= 0) {
            throw new RuleException("token is not available (anymore)");
        }
        availableTokens.put(token, available - 1);
        selectedToken = token;
    }

    // returns true on win
    public boolean place(Position pos) throws RuleException {
        checkFinished();
        if (!tokenSelected()) {
            throw new RuleException("no token has been selected");
        }
        if (!board.place(pos, selectedToken)) {
            takeSelectedToken();
            availableTokens.put(selectedToken, availableTokens.get(selectedToken) + 1);
            throw new RuleException("cannot place token on non-empty field");
        }
        takeSelectedToken();
        if (noMoreTokens()) {
            outOfTokens = true;
            finished = true;
        }
        playerWhoPlaced = playerWhoPlaced % 2 + 1;
        counter++;
        boolean won = board.winningState();
        if (won) {
            finished = true;
        }
        return won;
    }

    public String getAvailable() {
        Set<String> tokenStrings = new HashSet<>();
        for (Token tkn: getAvailableIntern()) {
            tokenStrings.add(tkn.toString());
        }
        return StringUtil.join(tokenStrings);
    }

    private Set<Token> getAvailableIntern() {
        Set<Token> available = new HashSet<>();
        for (Token tkn: availableTokens.keySet()) {
            if (availableTokens.get(tkn) > 0) {
                available.add(tkn);
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

    private boolean tokenSelected() {
        return selectedToken != null;
    }

    private void takeSelectedToken() {
        selectedToken = null;
    }

    private boolean noMoreTokens() {
        for (int i: availableTokens.values()) {
            if (i > 0) {
                return false;
            }
        }
        return true;
    }

    private void checkFinished() throws RuleException {
        if (finished) {
            throw new RuleException("game is finished");
        }
    }
}
