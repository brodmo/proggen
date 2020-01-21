package edu.kit.informatik.dawn.model;

import java.util.List;

/**
 * Modelliert das Spiel Dawn 11/15.
 * @author Moritz Brödel
 * @version 4.10
 */
public class Game {

    private static final int DIE_SIDES = 6;
    // Die DAWN Sonde ist automatisch auch immer die längste
    private static final int DAWN_PIECE_LENGTH = 7;

    private Board board;
    private MissionControl missionControl;
    private Nature nature;
    private Piece currentOrbPiece;
    private int currentRoll;
    private int placedProbeLength;
    private int turns;
    private Action expectedAction;
    private int shortestLength; // an sich redundant, aber macht den Code leserlicher
    
    /**
     * Erstellt ein neues Spiel.
     */
    public Game() {
        board = new Board();
        shortestLength = DAWN_PIECE_LENGTH - DIE_SIDES + 1;
        missionControl = new MissionControl(shortestLength, DAWN_PIECE_LENGTH);
        nature = new Nature();
        startAnew();
    }

    /**
     * Startet ein neues Spiel.
     */
    public final void startAnew() {
        board.empty();
        missionControl.resetPieces();
        nature.reset(); // fail fast (an sich nicht nötig)
        currentOrbPiece = nature.vesta();
        currentRoll = -9999; // fail fast
        placedProbeLength = -9999; // fail fast
        turns = 1;
        expectedAction = Action.PLACE_ORB;
    }

    /**
     * Würfelt den angegebenen Wurf.
     * @param roll Der gewürfelte Wurf. -1 für DAWN.
     * @throws RuleException Wenn roll nicht die laut den Spielregeln derzeit als nächstes durchzuführende Aktion ist
     *           sowie wenn der angegebene Wurf größer oder kleiner als möglich ist.
     */
    public void roll(int roll) throws RuleException {
        checkExpected(Action.ROLL);
        if (roll == -1) {
            currentRoll = DAWN_PIECE_LENGTH;
        } else if (roll < shortestLength || roll >= DAWN_PIECE_LENGTH) {
            throw new RuleException("roll bigger or smaller than allowed");
        } else {
            currentRoll = roll;
        }
        expectedAction = Action.PLACE_PROBE;
    }

    /**
     * Platziert den laut den Spielregeln derzeit zu platzierenden Himmelskörper auf das angegebene Feld.
     * @param field Das Feld, auf das der Himmelskörper platziert werden soll.
     * @throws RuleException Wenn Himmelskörper platzieren nicht die laut den Spielregeln derzeit als nächstes
     *           durchzuführende Aktion ist sowie wenn das angegebene Feld nicht auf dem Spielbrett oder
     *           nicht frei ist.
     */
    public void placeOrb(Field field) throws RuleException {
        // prüft, ob der Zug gültig ist
        checkExpected(Action.PLACE_ORB);
        checkInBounds(field);
        if (board.isOccupied(field)) {
            throw new RuleException("field is occupied");
        }
        // führt den Zug aus
        board.setState(field, currentOrbPiece.getIdentifier());
        currentOrbPiece.setField(field);
        expectedAction = Action.ROLL;
    }

    /**
     * Platziert die Sonde der entsprechenden Länge des Spielers Mission Control
     * auf die Felder zwischen den angegebenen, einschließlich dieser.
     * @param ends Die Felder, zwischen die die Sonde der entsprechenden Länge platziert werden soll.
     * @throws RuleException Wenn Sonde platzieren nicht die laut den Spielregeln derzeit als nächstes durchzuführende
     *           Aktion ist sowie wenn das Sonde platzieren mit den angegebenen Enden die Regeln verletzt,
     *           also etwa z.B. die Sonde dieser Länge nicht (mehr) verfügbar ist.
     */
    public void placeProbe(FieldTuple ends) throws RuleException {
        // prüft, ob der Zug gültig ist
        checkExpected(Action.PLACE_PROBE);
        List<Field> betweenFields = board.getBetweenFields(ends); // enthält die Enden
        if (betweenFields.size() == 0) {
            throw new RuleException("the specified fields are neither on the same row nor on the same column");
        }
        int probeLength = betweenFields.size();
        // Prüft, ob Mission Control den Sondenspielstein der angegebenen Länge hat und setzen darf.
        if (probeLength < shortestLength || probeLength > DAWN_PIECE_LENGTH) {
            throw new RuleException("you don't have a piece of that length");
        }
        if (!missionControl.pieceAvailable(probeLength)) {
            throw new RuleException("the piece you want to place has already been placed");
        }
        if (missionControl.pieceAvailable(currentRoll)) {
            if (probeLength != currentRoll) {
                throw new RuleException("the length of the piece you want to place does not equal the roll");
            }
        } else if (probeLength != missionControl.getNextBiggestAvailable(currentRoll)
                && probeLength != missionControl.getNextSmallestAvailable(currentRoll)) {
            throw new RuleException("the length of the piece you want to place is not the next closest to the roll "
                                        + "in either direction with the corresponding piece still available");
        }
        // Prüft, ob beide Enden innerhalb des Spielbretts sind. Wenn dies nicht der Fall ist wird nur dann keine
        // Exception geworfen, wenn nur ein Ende außerhalb des Spielbretts ist und der platzierte Stein DAWN ist.
        int outOfBoundsFields = 0;
        if (board.isOutOfBounds(ends.getFirstField())) {
            outOfBoundsFields++;
        }
        if (board.isOutOfBounds(ends.getSecondField())) {
            outOfBoundsFields++;
        }
        if (outOfBoundsFields == 2) {
            throw new RuleException("both ends out of bounds");
        } else if (outOfBoundsFields == 1 && probeLength != DAWN_PIECE_LENGTH) {
            throw new RuleException("only DAWN may be placed out of bounds");
        }
        // Prüft, ob alle Felder, auf die die Sonde platziert werden soll, frei sind.
        for (Field field: betweenFields) {
            if (!board.isOutOfBounds(field) && board.isOccupied(field)) {
                throw new RuleException("at least one of the fields is occupied");
            }
        }
        // führt den Zug aus
        missionControl.takePiece(probeLength);
        for (Field field: betweenFields) {
            if (!board.isOutOfBounds(field)) {
                board.setState(field, FieldState.PROBE);
            }
        }
        placedProbeLength = probeLength;
        // move wird übersprungen, wenn kein Zug möglich ist
        if (board.isEnclosed(currentOrbPiece.getField())) {
            endTurn();
        } else {
            expectedAction = Action.MOVE_ORB;
        }
    }

    /**
     * Bewegt den laut den Spielregeln derzeit zu bewegenden Himmelskörper entlang der angegebenen Felder.
     * @param fields Die Felder, entlang derer der Himmelskörper bewegt werden soll.
     * @throws RuleException Wenn Himmelskörper bewegen nicht die laut den Spielregeln derzeit als nächstes
     *           durchzuführende Aktion ist sowie wenn das Himmelskörper bewegen entlang der angegebenen Felder
     *           die Regeln verletzt, also etwa z.B. eines der angegebenen Felder besetzt ist.
     */
    public void moveOrb(List<Field> fields) throws RuleException {
        // prüft, ob der Zug gültig ist
        checkExpected(Action.MOVE_ORB);
        int movesLeft = placedProbeLength;
        Field conjecturedOrbField = currentOrbPiece.getField();
        for (Field field: fields) {
            checkInBounds(field);
            if (movesLeft <= 0) {
                throw new RuleException("more moves than allowed");
            }
            // der Spielstein soll sich natürlich nicht selbst blockieren
            if (board.isOccupied(field) && board.getState(field) != currentOrbPiece.getIdentifier()) {
                throw new RuleException("at least one of the fields is occupied");
            }
            if (!conjecturedOrbField.adjacentTo(field)) {
                throw new RuleException("all successive fields have to be adjacent");
            }
            movesLeft--;
            conjecturedOrbField = field;
        }
        // führt den Zug aus
        board.setState(currentOrbPiece.getField(), FieldState.EMPTY);
        board.setState(conjecturedOrbField, currentOrbPiece.getIdentifier());
        currentOrbPiece.setField(conjecturedOrbField);
        endTurn();
    }

    /**
     * Berechnet das Ergebnis des Spiels und gibt dieses zurück.
     * @return Das Ergebnis des Spiels als String.
     * @throws RuleException Wenn das Spiel noch läuft.
     */
    public String getResult() throws RuleException {
        if (expectedAction != Action.NONE) {
            throw new RuleException("the result can only be calculated once the game is over");
        }
        int vestaReachableAmount = board.getReachableAmount(nature.vesta().getField());
        int ceresReachableAmount = board.getReachableAmount(nature.ceres().getField());
        return Integer.toString(
                2 * Math.max(vestaReachableAmount, ceresReachableAmount)
                 - Math.min(vestaReachableAmount, ceresReachableAmount));
    }

    /**
     * Ermittelt den Zustand des angegebenen Felds und gibt diesen zurück.
     * @param field Das Feld, dessen Zustand ermittelt werden soll.
     * @return Den Zustand des angegebenen Felds in String Darstellung.
     * @throws RuleException Wenn das angegebene Feld nicht auf dem Spielbrett ist.
     */
    public String getState(Field field) throws RuleException {
        checkInBounds(field);
        return board.getState(field).toString();
    }

    @Override
    public String toString() {
        return board.toString();
    }

    // Prüft, ob die angegebene Aktion die laut den Spielregeln derzeit als nächstes durchzuführende Aktion ist
    // und wirft eine RuleException, falls nicht. Sollte nicht für NONE verwendet werden.
    private void checkExpected(Action action) throws RuleException {
        if (action != expectedAction) {
            if (expectedAction == Action.NONE) {
                throw new RuleException("the game is over, you cannot alter its state anymore");
            }
            throw new RuleException("unexpected action, expected " + expectedAction);
        }
    }

    // Prüft, ob das angegebene Feld auf dem Spielbrett ist und wirft eine RuleException, falls nicht.
    private void checkInBounds(Field field) throws RuleException {
        if (board.isOutOfBounds(field)) {
            throw new RuleException("out of bounds field(s) not allowed here");
        }
    }

    private void endTurn() {
        if (turns == 2 * DIE_SIDES) {
            expectedAction = Action.NONE;
            return;
        }
        if (turns == DIE_SIDES) {
            currentOrbPiece = nature.ceres();
            missionControl.resetPieces();
            expectedAction = Action.PLACE_ORB;
        } else {
            expectedAction = Action.ROLL;
        }
        turns++;
    }
}
