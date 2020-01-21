package edu.kit.informatik.dawn.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * Modelliert das Spielbrett.
 * @author Moritz Brödel
 * @version 3.5
 */
class Board {

    private static final int NUMBER_OF_BOARD_ROWS = 11;
    private static final int NUMBER_OF_BOARD_COLUMNS = 15;

    private FieldState[][] boardStates;

    /**
     * Erstellt ein neues Spielbrett.
     */
    Board() {
        boardStates = new FieldState[NUMBER_OF_BOARD_ROWS][NUMBER_OF_BOARD_COLUMNS];
        empty();
    }

    /**
     * Leert das Spielbrett.
     */
    final void empty() {
        for (int row = 0; row < NUMBER_OF_BOARD_ROWS; row++) {
            for (int col = 0; col < NUMBER_OF_BOARD_COLUMNS; col++) {
                boardStates[row][col] = FieldState.EMPTY;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(NUMBER_OF_BOARD_ROWS * (NUMBER_OF_BOARD_COLUMNS + 1));
        for (int row = 0; row < NUMBER_OF_BOARD_ROWS; row++) {
            for (int col = 0; col < NUMBER_OF_BOARD_COLUMNS; col++) {
                sb.append(boardStates[row][col]);
            }
            sb.append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * Ermittelt den Zustand des angegebenen Felds und gibt diesen zurück.
     * @param field Das Feld, dessen Zustand ermittelt werden soll.
     * @return Den Zustand des Felds.
     */
    FieldState getState(Field field) {
        return boardStates[field.getRow()][field.getCol()];
    }

    /**
     * Setzt das angegebene Feld dem angegebenen Zustand.
     * @param field Das Feld, dessen Zustand geändert werden soll.
     * @param state Der Zustand, den das Feld haben soll.
     */
    void setState(Field field, FieldState state) {
        boardStates[field.getRow()][field.getCol()] = state;
    }

    /**
     * Ermittelt, ob das angegebene Feld besetzt ist, und gibt das Ergebnis zurück.
     * @param field Das Feld, von dem überprüft werden soll, ob es besetzt ist.
     * @return Ob das angegebene Feld besetzt ist.
     */
    boolean isOccupied(Field field) {
        return getState(field) != FieldState.EMPTY;
    }

    /**
     * Ermittelt, ob das angegeben Feld nicht auf dem Spielbrett ist und gibt das Ergebnis zurück.
     * @param field Das Feld, von dem ermittelt werden soll, ob es nicht auf dem Spielbrett ist.
     * @return Ob das angegeben Feld nicht auf dem Spielbrett ist.
     */
    boolean isOutOfBounds(Field field) {
        return field.outOfBounds(NUMBER_OF_BOARD_ROWS, NUMBER_OF_BOARD_COLUMNS);
    }

    /**
     * Ermittelt, ob das angegebene Feld komplett direkt umschlossen ist und gibt das Ergebnis zurück.
     * Nicht auf dem Spielbrett liegende Felder werden als Barriere angesehen.
     * @param field Das Feld, dessen direkte Umschlossenheit ermittelt werden soll.
     * @return Ob das angegebene Feld komplett direkt umschlossen ist.
     */
    boolean isEnclosed(Field field) {
        for (Field adjField: field.getAdjacentFields()) {
            if (!isOutOfBounds(adjField) && !isOccupied(adjField)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ermittelt, wie viele andere Felder man von dem angegebenen Feld aus erreichen kann und gibt das Ergebnis zurück.
     * @param field Das Feld, für das ermittelt werden soll, wie viele Felder man von ihm aus erreichen kann.
     * @return Wie viele andere Felder man von dem angegebenen Feld aus erreichen kann.
     */
    int getReachableAmount(Field field) {
        Set<Field> reachableFields = new HashSet<>();
        floodFill(field, reachableFields);
        return reachableFields.size();
    }

    // Rekursiv, geht zuerst in die Tiefe. Der 2. Parameter verändert sich beim Selbstaufruf nicht.
    private void floodFill(Field field, Set<Field> reachableFields) {
        for (Field adjField : field.getAdjacentFields()) {
            if (!isOutOfBounds(adjField) && !isOccupied(adjField) && !reachableFields.contains(adjField)) {
                reachableFields.add(adjField);
                // ruft sich maximal NUMBER_OF_BOARD_ROWS * NUMBER_OF_BOARD_COLUMNS mal selbst auf
                floodFill(adjField, reachableFields);
            }
        }
    }

    /**
     * Ermittelt, welche Felder zwischen den beiden angegebenen liegen und gibt diese
     * sowie die beiden angegebenen Felder als Liste zurück.
     * @param fieldTuple Die beiden Felder, für die die zwischenliegenden ermittelt werden sollen.
     * @return Die Felder, die zwischen den beiden angegebenen Feldern liegen sowie die beiden angegebenen Felder
     * als Liste. Aufsteigend sortiert nach Zeile oder Spalte, je nachdem, was nicht bei allen Feldern in der Liste
     * gleich ist. Leere Liste, falls die angegebenen Felder weder in der selben Zeile noch in der selben Spalte sind.
     */
    List<Field> getBetweenFields(FieldTuple fieldTuple) {
        List<Field> betweenFields = new ArrayList<>();
        Field headField = fieldTuple.getFirstField();
        Field tailField = fieldTuple.getSecondField();
        if (fieldTuple.sameRow()) {
            int minCol = Math.min(headField.getCol(), tailField.getCol());
            int maxCol = Math.max(headField.getCol(), tailField.getCol());
            for (int col = minCol; col <= maxCol; col++) {
                betweenFields.add(new Field(headField.getRow(), col)); // headField.getRow() == tailField.getRow()

            }

        } else if (fieldTuple.sameCol()) {
            int minRow = Math.min(headField.getRow(), tailField.getRow());
            int maxRow = Math.max(headField.getRow(), tailField.getRow());
            for (int row = minRow; row <= maxRow; row++) {
                betweenFields.add(new Field(row, headField.getCol())); // headField.getCol() == tailField.getCol()
            }
        }
        return betweenFields;
    }
}
