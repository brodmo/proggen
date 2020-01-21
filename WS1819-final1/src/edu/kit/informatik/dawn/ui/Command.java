package edu.kit.informatik.dawn.ui;

import edu.kit.informatik.dawn.model.Game;
import edu.kit.informatik.dawn.model.RuleException;

import static edu.kit.informatik.dawn.ui.ArgumentParser.*;

public enum Command {
    PRINT("print") {
        public String execute(Game g, String argument) throws ParseException {
            assertNoArgument(argument);
            return g.toString();
        }
    },
    SHOW_RESULT("show-result") {
        public String execute(Game g, String argument) throws ParseException, RuleException {
            assertNoArgument(argument);
            return g.getResult();
        }
    },
    RESET("reset") {
        public String execute(Game g, String argument) throws ParseException {
            assertNoArgument(argument);
            g.startAnew();
            return OK;
        }
    },
    STATE("state") {
        public String execute(Game g, String argument) throws ParseException, RuleException {
            return g.getState(parseField(trimSpace(argument)));
        }
    },
    PLACE_ORB("set-vc") {
        public String execute(Game g, String argument) throws ParseException, RuleException {
            g.placeOrb(parseField(trimSpace(argument)));
            return OK;
        }
    },
    ROLL("roll") {
        public String execute(Game g, String argument) throws ParseException, RuleException {
            g.roll(parseRollArg(trimSpace(argument)));
            return OK;
        }
    },
    PLACE_PROBE("place") {
        public String execute(Game g, String argument) throws ParseException, RuleException {
            g.placeProbe(parsePlaceArg(trimSpace(argument)));
            return OK;
        }
    },
    MOVE_ORB("move") {
        public String execute(Game g, String argument) throws ParseException, RuleException {
            g.moveOrb(parseMoveArg(trimSpace(argument)));
            return OK;
        }
    },
    UNKNOWN("unknown") {
        public String execute(Game g, String argument) throws ParseException {
            throw new ParseException("unknown command");
        }
    };

    /**
     * Führt den Befehl aus und gibt sämtliche Ausgaben aus.
     * @param g Das Spiel, auf dem der Befehl ausgeführt werden soll.
     * @param argument Der Parameter des Befehls (soll "" wenn der Befehl keinen Parameter hat).
     * @return Der Output, den der Befehl erzeugt.
     */
    public abstract String execute(Game g, String argument) throws ParseException, RuleException;

    private static void assertNoArgument(String argument) throws ParseException {
        if (!argument.isEmpty()) {
            throw new ParseException("argument specified despite this command not having one");
        }
    }

    private static String trimSpace(String argument) throws ParseException {
        if (argument.isEmpty()) {
            throw new ParseException("no argument specified");
        }
        return argument.substring(1);
    }

    private static final String OK = "OK";

    private String string;

    Command(String string) {
        this.string = string;
    }

    public static Command matchCommand(String input) {
        for (Command cmd: Command.values()) {
            if (input.equals(cmd.string)) {
                return cmd;
            }
        }
        return UNKNOWN;
    }

    @Override
    public String toString() {
        return string;
    }
}
