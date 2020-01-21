package edu.kit.informatik.dawn.ui;

import edu.kit.informatik.dawn.model.Field;
import edu.kit.informatik.dawn.model.FieldTuple;

import java.util.List;
import java.util.ArrayList;

/**
 * Parset die Argumente der Eingaben.
 * @author Moritz Brödel
 * @version 3.5
 */
final class ArgumentParser {

    /**
     * Das/die Zeichen, das/die verwendet werden soll(en), um Koordinaten voneinander zu trennen.
     */
    private static final String COORDINATE_SEPARATOR = ";";
    /**
     * Das/die Zeichen, das/die verwendet werden soll(en), um Felder voneinander zu trennen.
     */
    private static final String FIELD_SEPARATOR = ":";
    /**
     * Der RegEx eines Integers ohne führende Nullen im mathematischen Sinne,
     * also einer nicht-negativen ganzen Zahl mit den Ziffern von 0-9.
     */
    private static final String INTEGER_RX = "[1-9][0-9]*";

    /**
     * Parset das Argument des roll Befehls und gibt die geparste Version zurück.
     * @param argument Das Argument, das geparset werden soll.
     * @return Der geparste Wurf als Zahl. -1 für DAWN.
     * @throws ParseException Wenn die Syntax der Eingabe nicht stimmt.
     */
    static int parseRollArg(String argument) throws ParseException {
        if (argument.matches("DAWN")) {
            return -1;
        }
        if (!argument.matches(INTEGER_RX)) {
            throw new ParseException("invalid or nonpositive roll");
        }
        try {
            return Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            throw new ParseException("far too big roll");
        }
    }

    /**
     * Parset ein Feld und gibt die geparste Version zurück.
     * @param str Der String, der geparset werden soll.
     * @return Das geparste Feld.
     * @throws ParseException Wenn die Syntax der Eingabe nicht stimmt.
     */
    static Field parseField(String str) throws ParseException {
        String coordinateRX = "((-?" + INTEGER_RX + ")|0)"; // erlaubt alle ganzen Zahlen ohne führende Nullen
        if (!str.matches(coordinateRX + COORDINATE_SEPARATOR + coordinateRX)) {
            throw new ParseException("invalid field(s)");
        }
        String[] coordinates = str.split(COORDINATE_SEPARATOR); // kein ,-1 nötig aufgrund der RegEx-Überprüfung
        try {
            return new Field(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
        } catch (NumberFormatException e) {
            throw new ParseException("far too big or small coordinate(s)");
        }
    }

    /**
     * Parset das Argument des place Befehls und gibt die geparste Version zurück.
     * @param argument Das Argument, das geparset werden soll.
     * @return Das geparste Tupel von Feldern.
     * @throws ParseException Wenn die Syntax der Eingabe nicht stimmt.
     */
    static FieldTuple parsePlaceArg(String argument) throws ParseException {
        String[] fields = argument.split(FIELD_SEPARATOR, -1);
        // nötig, um IndexOutOfBoundsException zu vermeiden, wenn input keinen COORDINATE_SEPARATOR enthält
        if (fields.length != 2) {
            throw new ParseException("wrong number of fields, expected two");
        }
        return new FieldTuple(parseField(fields[0]), parseField(fields[1])); // schmeißt ggf ParseException
    }

    /**
     * Parset das Argument des move Befehls und gibt die geparste Version zurück.
     * @param argument Das Argument, das geparset werden soll.
     * @return Die geparste ArrayList von Feldern.
     * @throws ParseException Wenn die Syntax der Eingabe nicht stimmt.
     */
    static List<Field> parseMoveArg(String argument) throws ParseException {
        List<Field> fields = new ArrayList<>();
        for (String fieldStr: argument.split(FIELD_SEPARATOR, -1)) {
            fields.add(parseField(fieldStr)); // schmeißt ggf ParseException
        }
        return fields;
    }
}
