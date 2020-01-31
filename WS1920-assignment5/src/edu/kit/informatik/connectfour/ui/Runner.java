package edu.kit.informatik.connectfour.ui;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.connectfour.model.Game;
import edu.kit.informatik.connectfour.model.RuleException;


/**
 * The type Runner.
 * @author The Nipster
 * @version 69.420
 */
class Runner {

    private Game game = new Game();

    /**
     * Run.
     */
    void run() {
        boolean over = false;
        do {
            String input = Terminal.readLine();
            try {
                over = processInput(input);
            } catch (ParseException e) {
                Terminal.printError("couldn't parse input: " + e.getMessage());
            } catch (RuleException e) {
                Terminal.printError("you broke the rules: " + e.getMessage());
            }
        } while (!over);
    }

    private boolean processInput(String input) throws ParseException, RuleException {
        String[] split = input.split(" ", -1);
        if (split.length > 2) {
            throw new ParseException("at most one space allowed");
        }
        String commandStr = split[0];
        Command command = Command.matchCommand(commandStr);
        String argument = input.substring(commandStr.length());
        String output = command.execute(argument, game);
        if (!output.equals(Command.NOTHING)) {
            Terminal.printLine(output);
        }
        return command == Command.QUIT;
    }
}
