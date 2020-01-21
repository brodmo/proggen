package edu.kit.informatik.dawn.model;

/**
 * Wird geworfen wenn eine Spielregel verletzt wird, d.h. die Semantik einer Eingabe ungültig ist.
 * @author Moritz Brödel
 * @version 1.0
 */
public class RuleException extends Exception {

    /**
     * Erstellt eine neue Spielregel-Exception mit der angegebenen Fehlernachricht.
     * @param message Die Fehlernachricht, die die Spielregel-Exception haben soll.
     */
    RuleException(String message) {
        super(message);
    }
}
