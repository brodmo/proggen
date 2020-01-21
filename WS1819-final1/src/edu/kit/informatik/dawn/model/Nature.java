package edu.kit.informatik.dawn.model;

/**
 * Modelliert den Spieler Nature.
 * @author Moritz Brödel
 * @version 1.1
 */
class Nature {
// Keine Oberklasse/Interface Player weil Nature nichts mit Mission Control gemeinsam hat.

    private Piece vesta;
    private Piece ceres;

    /**
     * Erstellt einen neuen Spieler Nature.
     */
    Nature() {
        vesta = new Piece(FieldState.VESTA);
        ceres = new Piece(FieldState.CERES);
    }

    /**
     * @return Vesta dieses Spielers.
     */
    Piece vesta() {
        return vesta;
    }

    /**
     * @return Ceres dieses Spielers.
     */
    Piece ceres() {
        return ceres;
    }

    /**
     * Setzt den Spieler zurück. An sich keine Funktionalität, aber vermeidet Fehler.
     */
    void reset() {
        vesta.reset();
        ceres.reset();
    }
}
