package edu.kit.informatik.dawn.model;

/**
 * Modelliert 1x1, also ein Feld besetzende, Spielsteine. Wird für die beiden Himmelskörper Vesta und Ceres verwendet.
 * @author Moritz Brödel
 * @version 1.3
 */
class Piece {

    private Field field;
    private FieldState identifier;

    /**
     * Erstellt einen neuen Spielstein mit der angegebenen Kennung.
     * @param identifier Welche Kennung der Spielstein haben soll.
     */
    Piece(FieldState identifier) {
        this.identifier = identifier;
    }

    /**
     * @return Das Feld, auf dem sich der Spielstein aktuell befindet.
     */
    Field getField() {
        return field;
    }

    /**
     * Bewegt den Spielstein auf das angegebene Feld.
     * @param field Das Feld, auf das der Spielstein bewegt werden soll.
     */
    void setField(Field field) {
        this.field = field;
    }

    /**
     * @return Welche Kennung der Spielstein hat.
     */
    FieldState getIdentifier() {
        return identifier;
    }

    /**
     * Setzt den Spielstein zurück. An sich keine Funktionalität, aber vermeidet Fehler.
     */
    void reset() {
        field = null;
    }
}
