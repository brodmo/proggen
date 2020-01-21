package edu.kit.informatik.dawn.model;

/**
 * Modelliert ein Zweiertupel von Spielfeldern.
 * @author Moritz Brödel
 * @version 2.1
 */
public class FieldTuple {

    private Field firstField;
    private Field secondField;

    /**
     * Erstellt ein neues Feldtupel mit den angegebenen Feldern.
     * @param firstField Das erste Feld des Tupels.
     * @param secondField Das zweite Feld des Tupels.
     */
    public FieldTuple(Field firstField, Field secondField) {
        this.firstField = firstField;
        this.secondField = secondField;
    }

    /**
     * @return Das erste Feld des Tupels.
     */
    Field getFirstField() {
        return firstField;
    }

    /**
     * @return Das zweite Feld des Tupels.
     */
    Field getSecondField() {
        return secondField;
    }

    /**
     * @return Ob die beiden Felder des Tupels in der selben Zeile sind.
     */
    boolean sameRow() {
        return firstField.sameRowAs(secondField);
    }

    /**
     * @return Ob die beiden Felder des Tupels in der selben Spalte sind.
     */
    boolean sameCol() {
        return firstField.sameColAs(secondField);
    }
}
