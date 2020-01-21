package edu.kit.informatik.dawn.model;

import java.util.Objects;

/**
 * Modelliert ein Spielfeld. Dieses kann auch außerhalb des Spielbretts liegen.
 * @author Moritz Brödel
 * @version 2.2
 */
public class Field {

    private int row;
    private int col;

    /**
     * Erstellt ein neues Feld mit den angegebenen Koordinaten.
     * @param row Die Zeile, auf der sich das Feld befinden soll.
     * @param col Die Spalte, auf der sich das Feld befinden soll.
     */
    public Field(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        Field field = (Field) obj;
        return field.getRow() == row && field.getCol() == col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    /**
     * @return Die Zeile, auf der sich das Feld befindet.
     */
    int getRow() {
        return row;
    }

    /**
     * @return Die Spalte, auf der sich das Feld befindet.
     */
    int getCol() {
        return col;
    }

    /**
     * Ermittelt, ob sich das Feld nicht auf dem Spielbrett mit den angegebenen Dimensionen befindet
     * und gibt das Ergebnis zurück.
     * @param numberOfRows Die Anzahl der Zeilen des Spielbretts.
     * @param numberOfColumns Die Anzahl der Spalten des Spielbretts.
     * @return Ob sich das Feld nicht auf dem Spielbrett mit der angegebenen Anzahl an Zeilen und Spalten befindet.
     */
    boolean outOfBounds(int numberOfRows, int numberOfColumns) {
    // Diese beiden Konstanten werden hier mitgegeben, da man sie ansonsten, wenn man sie ändern wollte,
    // in mehreren Klassen ändern müsste. (Stichwort Redundanz)
        return row < 0 || col < 0 || row >= numberOfRows || col >= numberOfColumns;
    }

    /**
     * Ermittelt die dem Feld adjazenten Felder und gibt sie als Array zurück.
     * @return Die dem Feld adjazenten Felder als Array.
     * Reihenfolge: Das rechts-adjazente Feld ist das erste, die anderen 3 folgen im Uhrzeigersinn.
     */
    Field[] getAdjacentFields() {
        Field[] adjacentFields = new Field[4];
        adjacentFields[0] = new Field(row, col + 1);
        adjacentFields[1] = new Field(row + 1, col);
        adjacentFields[2] = new Field(row, col - 1);
        adjacentFields[3] = new Field(row - 1, col);
        return adjacentFields;
    }

    /**
     * Ermittelt, ob das Feld in der selben Zeile ist wie das angegebene andere und gibt das Ergebnis zurück.
     * @param field Das andere Feld, von dem ermittelt werden soll, ob es in der selben Zeile ist.
     * @return Ob das Feld in der selben Zeile ist wie das angegebene andere.
     */
    boolean sameRowAs(Field field) {
        return row == field.getRow();
    }

    /**
     * Ermittelt, ob das Feld in der selben Spalte ist wie das angegebene andere und gibt das Ergebnis zurück.
     * @param field Das andere Feld, von dem ermittelt werden soll, ob es in der selben Spalte ist.
     * @return Ob das Feld in der selben Spalte ist wie das angegebene andere.
     */
    boolean sameColAs(Field field) {
        return col == field.getCol();
    }

    /**
     * Ermittelt, ob das Feld adjazent zu dem angegebenen anderen ist und gibt das Ergebnis zurück.
     * @param field Das andere Feld, von dem ermittelt werden soll, ob es adjazent ist.
     * @return Ob das andere Feld adjazent ist.
     */
    boolean adjacentTo(Field field) {
        return (sameRowAs(field) && oneApart(col, field.getCol()))
                || (sameColAs(field) && oneApart(row, field.getRow()));
    }

    private boolean oneApart(int a, int b) {
        return a == b - 1 || a == b + 1;
    }
}
