package edu.kit.informatik.dawn.ui;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.dawn.model.Game;
import edu.kit.informatik.dawn.model.RuleException;

/**
 * Eingangspunkt. Lässt das Spiel laufen, nimmt und verarbeitet alle Eingaben und macht alle Ausgaben.
 * @author Moritz Brödel
 * @version 1.6
 */
public class Main {

    /**
     * Main Methode. Startet das Spiel.
     * @param args String Array der auf der Konsole angegebenen Argumente. Wird nicht verwendet.
     */
    public static void main(String[] args) {
        run();
    }

    /**
     * Startet ein neues Spiel und lässt dieses laufen,
     * nimmt und verarbeitet alle Eingaben und macht alle Ausgaben.
     */
    private static void run() {
        Game g = new Game();
        String input = Terminal.readLine();
        while (!input.equals("quit")) {
            try {
                int numberOfSpaces = input.length() - input.replace(" ", "").length();
                if (numberOfSpaces > 1) {
                    throw new ParseException("more than one space");
                }
                String command = input.split(" ", -1)[0];
                String argument = input.substring(command.length());
                Terminal.printLine(Command.matchCommand(command).execute(g, argument));
            } catch (ParseException e) {
                Terminal.printError("wrong input format: " + e.getMessage());
            } catch (RuleException e) {
                Terminal.printError("you broke the rules: " + e.getMessage());
            }
            input = Terminal.readLine();
        }
    }
}
