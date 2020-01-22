package edu.kit.informatik.connectfour.ui;

import edu.kit.informatik.connectfour.model.Game;
import edu.kit.informatik.connectfour.model.RuleException;
import edu.kit.informatik.connectfour.model.board.Board;
import edu.kit.informatik.connectfour.model.board.Position;
import edu.kit.informatik.connectfour.model.token.Token;

import java.util.Map;

import static edu.kit.informatik.connectfour.model.Game.NUMBER_OF_TOKENS;

public enum Command {
    START("start", false) {
        @Override
        String executeIntern(String argument, Game game) throws ParseException {
            Board board = parseBoard(trimSpace(argument));
            game.reset();
            game.setBoard(board);
            return NOTHING;
        }
    },
    SELECT("select") {
        @Override
        String executeIntern(String argument, Game game) throws ParseException, RuleException {
            game.take(parseToken(trimSpace(argument)));
            return OK;
        }
    },
    PLACE("place") {
        @Override
        String executeIntern(String argument, Game game) throws ParseException, RuleException {
            // too much kerfuffle with strings to do this in backend (in my humble opinion)
            if (game.place(parsePosition(trimSpace(argument)))) {
                return "P" + game.getPlayerWhoPlaced() + " wins\n" + game.getCounter();
            } else if (game.outOfTokens()) {
                return "draw";
            } else {
                return OK;
            }
        }
    },
    BAG("bag") {
        @Override
        String executeIntern(String argument, Game game) throws ParseException {
            checkNoArgument(argument);
            return game.getAvailable();
        }
    },
    PRINT_ROW("rowprint") {
        @Override
        String executeIntern(String argument, Game game) throws ParseException, RuleException {
            return game.rowToString(parseInt(trimSpace(argument)));
        }
    },
    PRINT_COL("colprint") {
        @Override
        String executeIntern(String argument, Game game) throws ParseException, RuleException {
            return game.colToString(parseInt(trimSpace(argument)));
        }
    },
    QUIT("quit", false) {
        @Override
        String executeIntern(String argument, Game game) throws ParseException {
            checkNoArgument(argument);
            return NOTHING;
        }
    };

    public static final String COORDINATE_SEPARATOR = ";";
    public static final String OK = "OK";
    public static final String NOTHING = "";

    abstract String executeIntern(String argument, Game game) throws ParseException, RuleException;

    final String execute(String argument, Game game) throws ParseException, RuleException {
        if (boardHasToBeSet && !game.boardSet()) {
            throw new RuleException("you have to start the game and set a board type first!");
        }
        return executeIntern(argument, game);
    }

    private String string;
    private boolean boardHasToBeSet;

    Command(String string) {
        this.string = string;
        this.boardHasToBeSet = true; // since this is the "default" behavior for a command
    }

    Command(String string, boolean boardHasToBeSet) {
        this.string = string;
        this.boardHasToBeSet = boardHasToBeSet;
    }

    @Override
    public String toString() {
        return string;
    }

    public static Command matchCommand(String command) throws ParseException {
        for (Command cmd: Command.values()) {
            if (cmd.toString().equals(command)) {
                return cmd;
            }
        }
        throw new ParseException("no command of this name found");
    }

    private static String trimSpace(String argument) {
        return argument.substring(1); // we know there is a space at index 0
    }

    private static void checkNoArgument(String argument) throws ParseException {
        if (!argument.isEmpty()) {
            throw new ParseException("unexpected argument or trailing space");
        }
    }

    private static int parseInt(String argument) throws ParseException {
        try {
            return Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            throw new ParseException("could not parse number describing token");
        }
    }

    private static Board parseBoard(String argument) throws ParseException {
        Map<String, Board> availableMap = Board.getAvailableBoards();
        if (availableMap.containsKey(argument)) {
            return availableMap.get(argument);
        } else {
            throw new ParseException("unknown board type");
        }
    }

    private static Token parseToken(String argument) throws ParseException {
        int number;
        try {
            number = Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            throw new ParseException("could not parse number describing token");
        }
        if (number < 0 || number >= NUMBER_OF_TOKENS) {
            throw new ParseException("invalid number describing token");
        }
        return new Token(number);
    }

    private static Position parsePosition(String argument) throws ParseException {
        String[] split = argument.split(COORDINATE_SEPARATOR);
        if (split.length != 2) {
            throw new ParseException("not exactly 2 coordinates specified");
        }
        try {
            return new Position(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        } catch (NumberFormatException e) {
            throw new ParseException("could not parse coordinates");
        }
    }
}
