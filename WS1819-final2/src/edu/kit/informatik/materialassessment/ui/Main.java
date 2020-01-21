package edu.kit.informatik.materialassessment.ui;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.materialassessment.model.AssessException;
import edu.kit.informatik.materialassessment.model.Assessor;


/**
 * Eingangspunkt. Lässt den Ermittler laufen, nimmt und verarbeitet alle Eingaben und macht alle Ausgaben.
 * @author Moritz Brödel
 * @version 2.4
 */
public class Main {

    /**
     * Main Methode. Startet den Ermittler.
     * @param args String Array der auf der Konsole angegebenen Argumente. Wird nicht verwendet.
     */
    public static void main(String[] args) {
        run();
    }

    /**
     * Startet einen neuen Ermittler und lässt diesen laufen,
     * nimmt und verarbeitet alle Eingaben und macht alle Ausgaben.
     */
    private static void run() {
        Assessor a = new Assessor();
        String input = Terminal.readLine();
        while (!input.equals("quit")) {
            try {
                String[] parts = Utility.splitInTwo(input, " ");
                String command = parts[0];
                String argument = parts[1];
                Terminal.printLine(Command.matchCommand(command).execute(a, argument));
            } catch (ParseException e) {
                Terminal.printError("wrong input format: " + e.getMessage());
            } catch (AssessException e) {
                Terminal.printError("invalid operation: " + e.getMessage());
            }
            input = Terminal.readLine();
        }
    }
}