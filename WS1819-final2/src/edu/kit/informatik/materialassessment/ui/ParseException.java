package edu.kit.informatik.materialassessment.ui;

/**
 * Wird geworfen wenn die Syntax einer Eingabe nicht stimmt.
 * @author Moritz Brödel
 * @version 1.0
 */
class  ParseException extends Exception {

    /**
     * Erstellt eine neue Parse-Exception mit der angegebenen Fehlernachricht.
     * @param message Die Fehlernachricht, die die Eingabe-Exception haben soll.
     */
    ParseException(String message) {
        super(message);
    }
}
