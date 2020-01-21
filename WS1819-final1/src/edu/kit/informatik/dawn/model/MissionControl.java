package edu.kit.informatik.dawn.model;

/**
 * Modelliert den Spieler Mission Control.
 * @author Moritz Brödel
 * @version 1.7
 */
class MissionControl {
// Keine Oberklasse/Interface Player weil Mission Control nichts mit Nature gemeinsam hat.

    private int dawnLength;
    private int shortestLength;
    private boolean[] pieceAvailable;

    /**
     * Erstellt einen neuen Spieler Mission Control mit den angegebenen Parametern.
     * @param shortestLength Wie lang der kürzeste Spielstein sein soll.
     * @param dawnLength Wie lang der längste Spielstein, also DAWN, sein soll.
     */
    MissionControl(int shortestLength, int dawnLength) {
        this.dawnLength = dawnLength;
        this.shortestLength = shortestLength;
        int numberOfPieces = dawnLength - shortestLength + 1;
        pieceAvailable = new boolean[numberOfPieces];
        resetPieces();
    }

    /**
     * Gibt dem Spieler alle seine Spielsteine zurück.
     */
    final void resetPieces() {
        for (int pieceIndex = 0; pieceIndex < pieceAvailable.length; pieceIndex++) {
            pieceAvailable[pieceIndex] = true;
        }
    }

    /**
     * Ermittelt, ob der Spieler den Spielstein der angegebenen Länge hat und gibt das Ergebnis zurück.
     * @param length Die Länge des Spielsteins, von dem ermittelt werden soll, ob der Spieler ihn hat.
     * @return Ob der Spieler den Spielstein der angegebenen Länge hat.
     */
    boolean pieceAvailable(int length) {
        return pieceAvailable[length - shortestLength];
    }

    /**
     * Nimmt den Spielstein der angegebenen Länge.
     * @param length Die Länge des Spielsteins, der genommen werden soll.
     */
    void takePiece(int length) {
        pieceAvailable[length - shortestLength] = false;
    }

    /**
     * Ermittelt, was die Länge des Spielsteins mit der dem angegebenen Wurf nächstgelegenen, größeren Länge ist,
     * der verfügbar ist und gibt das Ergebnis zurück.
     * @param roll Der Wurf.
     * @return Die gesuchte Spielsteinlänge.
     *          -1 wenn kein Spielstein mit einer dem Wurf echt größeren Länge verfügbar ist.
     */
    int getNextBiggestAvailable(int roll) {
        for (int pieceLength = roll + 1; pieceLength <= dawnLength; pieceLength++) {
            if (pieceAvailable(pieceLength)) {
                return pieceLength;
            }
        }
        return -1;
    }

    /**
     * Ermittelt, was die Länge des Spielsteins mit der dem angegebenen Wurf nächstgelegenen, kleineren Länge ist,
     * der verfügbar ist und gibt das Ergebnis zurück.
     * @param roll Der Wurf.
     * @return Die gesuchte Spielsteinlänge.
     *          -1 wenn kein Spielstein mit einer dem Wurf echt kleineren Länge verfügbar ist.
     */
    int getNextSmallestAvailable(int roll) {
        for (int pieceLength = roll - 1; pieceLength >= shortestLength; pieceLength--) {
            if (pieceAvailable(pieceLength)) {
                return pieceLength;
            }
        }
        return -1;
    }
}
