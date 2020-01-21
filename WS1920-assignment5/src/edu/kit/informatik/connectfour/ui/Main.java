package edu.kit.informatik.connectfour.ui;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.connectfour.model.Game;
import edu.kit.informatik.connectfour.model.RuleException;

public final class Main {

    public static void main(String[] args) {
        Game game = new Game();
        String input = Terminal.readLine();
        while(!input.equals("quit")) {
            try {
                String[] split = input.split(" ", -1);
                if (split.length > 2) {
                    throw new ParseException("at most one space allowed");
                }
                String commandStr = split[0];
                String argument = split.length == 1 ? "" : split[1];
                Command command = Command.matchCommand(commandStr);
                if (command != Command.START && !game.boardTypeSet()) {
                    throw new RuleException(
                            "you have to start the game and set a board type first!");
                }
                String output = command.execute(argument, game);
                if (!output.isEmpty()) {
                    Terminal.printLine(output);
                }
            } catch (RuleException e) {
                Terminal.printError("you broke the rules: " + e.getMessage());
            } catch (ParseException e) {
                Terminal.printError("couldn't parse input: " + e.getMessage());
            }
            input = Terminal.readLine();
        }
    }
}
