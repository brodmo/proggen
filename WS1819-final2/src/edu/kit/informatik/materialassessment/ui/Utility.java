package edu.kit.informatik.materialassessment.ui;

import java.util.regex.Pattern;

class Utility {
    /**
     * Überprüft, ob der angegeben String das angegebene Token genau einmal enthält und wirft eine ParseException
     * falls nicht. Gibt den entlang des Tokens in zwei gesplitteten String als Array zurück falls doch.
     * @param string Der String, der überprüft ung gesplittet werden soll.
     * @param separator Der/die Zeichen, das/die zum Trennen der Hälften verwendet werden soll,
     *                  also dessen Vorkommen überprüft und entlang dessen gesplittet werden soll.
     * @return Den entlang des Tokens in zwei gesplitteten String als Array.
     * @throws ParseException Wenn der String das Token nicht genau einmal enthält.
     */
    static String[] splitInTwo(String string, String separator) throws ParseException {
        String tokenString;
        if (separator.equals("-")) {
            // hardcoded, da getName('-') = "hyphen minus" ist. Muss, je nachdem, welche Zeichen man verwenden will,
            // ggf erweitert werden, wenn man weiterhin schöne Fehlermeldungen haben will.
            tokenString = "minus signs";
        } else if (separator.length() == 1) {
            tokenString = Character.getName(separator.charAt(0)).toLowerCase() + "s"; // Plural
        } else {
            tokenString = "\"" + separator + "\"";
        }
        if (string.length() - string.replace(separator, "").length() != 1) {
            throw new ParseException("wrong number of " + tokenString + ", expected one");
        }
        return string.split(Pattern.quote(separator), -1);
    }
}
