package edu.kit.informatik.materialassessment.model;

/**
 * Modelliert einen Posten an Teilen. Wird im Modell nur zum Ermitteln der
 * indirekt enthaltenen Baupläne und Einzelteile genutzt.
 * @author Moritz Brödel
 * @version 1.5
 */
public class PartBatch implements Comparable<PartBatch> {

    private final String name;
    private final long quantity;

    /**
     * Erstellt einen neuen Posten an Teilen.
     * @param name Der Name, den das Teil haben soll.
     * @param quantity Die Quantität, in der das Teil in dem Posten vorkommen soll.
     */
    PartBatch(String name, long quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    /**
     * @return Den Namen des Teils.
     */
    public String name() {
        return name;
    }

    /**
     * @return Wie oft das Teil in dem Posten vorkommt.
     */
    public long quantity() {
        return quantity;
    }

    @Override
    public int compareTo(PartBatch ptb) {
        int quantityComparison = Long.compare(ptb.quantity, quantity); // so rum, damit es absteigend ist
        if (quantityComparison == 0) {
            return name.compareTo(ptb.name);
        }
        return quantityComparison;
    }
}
