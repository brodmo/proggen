package edu.kit.informatik.materialassessment.model;

/**
 * Wird geworfen wenn eine Operation unzulässig ist, d.h. die Semantik einer Eingabe ungültig ist.
 * @author Moritz Brödel
 * @version 1.0
 */
public class AssessException extends  Exception {

    /**
     * Erstellt eine neue Ermittlungs-Exception mit der angegebenen Fehlernachricht.
     * @param message Die Fehlernachricht, die die Ermittlungs-Exception haben soll.
     */
    AssessException(String message) {
        super(message);
    }
}
