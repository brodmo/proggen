package edu.kit.informatik.WS1920_ÜB5A1_game.io;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.WS1920_ÜB5A1_game.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static edu.kit.informatik.WS1920_ÜB5A1_game.model.Game.NUMBER_OF_TOKENS;

public enum Command {
    START("start") {
        @Override
        String execute(String argument, Game game) throws IoException, RuleException {
            BoardType boardType = parseBoardType(argument);
            game.reset();
            game.setBoardType(boardType);
            return OK;
        }
    },
    SELECT("select") {
        @Override
        String execute(String argument, Game game) throws IoException, RuleException {
            game.take(parseToken(argument));
            return OK;
        }
    },
    PLACE("place") {
        @Override
        String execute(String argument, Game game) throws IoException, RuleException {
            if (game.place(parsePosition(argument))) {
                return "P" + game.getPlacingPlayer() + " wins";
            } else if (game.outOfTokens()) {
                return "draw";
            } else {
                return OK;
            }
        }
    },
    BAG("bag") {
        @Override
        String execute(String argument, Game game) throws IoException, RuleException {
            Set<Token> available = game.getAvailable();
            StringBuilder sb = new StringBuilder();
            for (Token token: available) {
                sb.append(token.toNumber());
                sb.append(" ");
            }
            return sb.substring(0, sb.length() - 1);
        }
    },
    PRINT_ROW("rowprint") {
        @Override
        String execute(String argument, Game game) throws IoException, RuleException {
            return game.rowToString(parseInt(argument));
        }
    },
    PRINT_COL("colprint") {
        @Override
        String execute(String argument, Game game) throws IoException, RuleException {
            return game.colToString(parseInt(argument));
        }
    };

    public static Command matchCommand(String command) throws IoException {
        for (Command cmd: Command.values()) {
            if (cmd.toString().equals(command)) {
                return cmd;
            }
        }
        throw new IoException("no command of this name found");
    }

    public static final String COORDINATE_SEPERATOR = ";";
    abstract String execute(String argument, Game game) throws IoException, RuleException;

    public static final String OK = "OK";

    private String string;

    Command(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }

    private static int parseInt(String argument) throws IoException {
        try {
            return Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            throw new IoException("could not parse number describing token");
        }
    }

    private static BoardType parseBoardType(String argument) throws IoException {
        if (argument.equals("standard")) {
            return BoardType.STANDARD;
        } else if (argument.equals("torus")) {
            return BoardType.TORUS;
        } else {
            throw new IoException("unknown board type");
        }
    }

    private static Token parseToken(String argument) throws IoException {
        int number;
        try {
            number = Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            throw new IoException("could not parse number describing token");
        }
        if (number < 0 || number > NUMBER_OF_TOKENS) {
            throw new IoException("invalid number describing token");
        }
        return Token.numberToToken(number);
    }

    private static Position parsePosition(String argument) throws IoException {
        String[] split = argument.split(COORDINATE_SEPERATOR, -1);
        if (split.length != 2) {
            throw new IoException("not exactly 2 coordinates specified");
        }
        int row;
        int col;
        try {
            row = Integer.parseInt(split[0]);
            col = Integer.parseInt(split[1]);
        } catch (NumberFormatException e) {
            throw new IoException("could not parse coordinates");
        }
        return new Position(row, col);
    }
}
