package edu.kit.informatik.materialassessment.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Arrays;

/**
 * Modelliert den Ermittler für den Materialbedarf.
 * @author Moritz Brödel
 * @version 4.4
 */
public class Assessor {

    /**
     * Die größte erlaubte Mengenangabe innerhalb einer Materialstückliste.
     */
    private static final int MAX_QUANTITY = 1000;
    /**
     * Der Regex der erlaubten Namen.
     */
    private static final String ALLOWED_NAMES_RX = "[a-zA-Z]+";

    /*
    Mapt den Namen jedes Bauplans zu seiner Materialstückliste, diese mapt wiederum jedes verbaute Bestandteil
    zu der Quantität, in der es direkt verbaut wird. Die Materialstücklisten sind jeweils aufsteigend nach
    Unicodewerten (d.h. alphabetisch, case sensitive) nach den Namen der zugehörigen Teile sortiert.
    */
    private Map<String, SortedMap<String, Integer>> allBoms;
    /*
     Mapt, wie oft jedes Teil in allen Bauplänen gesamt direkt benutzt wird (jede Verwendung zählt nur einfach,
     ungeachtet der angegebenen Quantität in der Materialstückliste des jeweiligen Bauplans).
     0 ist ein zulässiger Wert, wenn es sich bei dem Teil um einen Bauplan handelt.
     An sich redundant, erspart jedoch häufiges und aufwändiges Durchiteriern durch allBoms.
     */
    private Map<String, Integer> partsUsage;

    /**
     * Erstellt einen neuen Ermittler.
     */
    public Assessor() {
        allBoms = new HashMap<>();
        partsUsage = new HashMap<>();
    }

    /**
     * Fügt einen neuen Bauplan mit der angegebenen Materialstückliste ins System ein.
     * @param name Den Namen, den der Bauplan haben soll.
     * @param billOfMaterial Die Materialstückliste, die der Bauplan haben soll.
     * @throws AssessException Wenn die Operation mit den angegebenen Parametern unzulässig ist,
     *                         etwa weil z.B. schon ein Bauplan mit dem angegebenen Namen im System ist.
     */
    public void addAssembly(String name, SortedMap<String, Integer> billOfMaterial) throws AssessException {
        // überprüfen
        if (billOfMaterial.isEmpty()) { // passiert im Rahmen meiner Implementierung nicht
            throw new AssessException("empty bill of material");
        }
        checkName(name);
        if (allBoms.containsKey(name)) {
            throw new AssessException("an assembly with this name already exists");
        }
        for (String part: billOfMaterial.keySet()) {
            checkName(part);
            checkQuantity(billOfMaterial.get(part));
        }
        checkContainsSelf(name, billOfMaterial);
        // durchführen
        allBoms.put(name, billOfMaterial);
        add(partsUsage, name, 0);
        for (String part: billOfMaterial.keySet()) {
            add(partsUsage, part, 1);
        }
    }

    /**
     * Entfernt den Bauplan des angegebenen Namens aus dem System.
     * @param name Der Name des Bauplans, der entfernt werden soll.
     * @throws AssessException Wenn es keinen Bauplan mit dem angegebenen Namen gibt.
     */
    public void removeAssembly(String name) throws AssessException {
        checkAssemblyExistence(name);
        for (String part: allBoms.get(name).keySet()) {
            updatePartsUsageOnRemoval(part);
        }
        allBoms.remove(name);
        if (partsUsage.get(name) == 0) {
            partsUsage.remove(name);
        }
    }

    /**
     * Gibt die Materialstückliste für den Bauplan oder das Einzeilteil mit dem angegebenen Namen zurück,
     * aufsteigend nach Unicodewerten (d.h. alphabetisch, case sensitive) nach den Namen der zugehörigen Teile
     * sortiert. Die Materialstückliste ist leer wenn es sich bei dem Teil um ein Einzelteil handelt.
     * @param name Der Name des Bauplans oder Einzelteils, dessen Materialstückliste zurückgegeben werden soll.
     * @return Die Materialstückliste des angegebenen Bauplans, wenn das angegebene Teil ein Bauplan ist,
     *         aufsteigend nach Unicodewerten (d.h. alphabetisch, case sensitive) nach den Namen der zugehörigen Teile
     *         sortiert. Leere Map wenn es sich bei dem Teil um ein Einzelteil handelt.
     * @throws AssessException Wenn es keinen Bauplan oder Einzelteil mit dem angegebenen Namen gibt.
     */
    public SortedMap<String, Integer> getBom(String name) throws AssessException {
        if (!partsUsage.containsKey(name)) {
            throw new AssessException("no part with this name exists");
        }
        return allBoms.getOrDefault(name, new TreeMap<>());
    }

    /**
     * Gibt je nach Angabe entweder die Quantitäten und Namen aller Baupläne oder Einzelteile des Bauplans
     * mit dem angegebenen Namen zurück, absteigend sortiert nach der Quantität, aufsteigend nach Unicodewerten
     * (d.h. alphabetisch, case sensitive) nach dem Namen sortiert falls die Quantität gleich ist.
     * @param name Der Name des Bauplans, von dem man diese Information erfahren möchte.
     * @param getAssemblies Ob die Quantitäten und Namen aller Baupläne oder Einzelteile zurückgegeben werden soll.
     *                      true für Baupläne, false für Einzelteile. (d.h. false bedeutet getComponents)
     * @return Die Quantitäten und Namen aller Baupläne oder Einzelteile des Bauplans, absteigend sortiert nach der
     *         Quantität, aufsteigend nach Unicodewerten (d.h. alphabetisch, case sensitive) nach dem Namen sortiert
     *         falls die Quantität gleich ist.
     * @throws AssessException Wenn kein Bauplan mit dem angegebenen Namen existiert.
     */
    public PartBatch[] getPartBatches(String name, boolean getAssemblies) throws AssessException {
        checkAssemblyExistence(name);
        Map<String, Long> partsCounter = new HashMap<>();
        Queue<PartBatch> assembliesToCount = new LinkedList<>();
        Map<String, Integer> currentBom = allBoms.get(name);
        for (String part: currentBom.keySet()) {
            assembliesToCount.add(new PartBatch(part, currentBom.get(part)));
        }
        PartBatch ptb;
        while (!assembliesToCount.isEmpty()) {
            ptb = assembliesToCount.poll();
            if (allBoms.containsKey(ptb.name())) {
                // falls die Baupläne gesucht sind
                if (getAssemblies) {
                    add(partsCounter, ptb);
                }
                // Bestandteile zur Queue hinzufügen
                currentBom = allBoms.get(ptb.name());
                for (String subPart: currentBom.keySet()) {
                    assembliesToCount.add(new PartBatch(subPart, currentBom.get(subPart) * ptb.quantity()));
                }
            } else if (!getAssemblies) {
                // falls die Einzelteile gesucht sind
                add(partsCounter, ptb);
            }
        }
        PartBatch[] partBatches = new PartBatch[partsCounter.size()];
        int index = 0;
        for (String part: partsCounter.keySet()) {
            partBatches[index] = new PartBatch(part, partsCounter.get(part));
            index++;
        }
        Arrays.sort(partBatches);
        return partBatches;
    }

    private void add(Map<String, Long> partsCounter, PartBatch ptb) {
        partsCounter.put(ptb.name(), partsCounter.getOrDefault(ptb.name(), 0L) + ptb.quantity());
    }

    /**
     * Ändert die Quantität des angegebenen Teils in dem angegegebenen Bauplan um die angegebene.
     * @param assembly Der Bauplan, in dem die Quantität des angegebenen Teils um die angegebene geändert werden soll.
     * @param part Das Teil, dessen Quantität in dem angegebenen Bauplan um die angegebenene geändert werden soll.
     * @param quantityToChangeBy Die Quantität, um die das Vorkommen des angegebenen Teils in dem angegebenen Bauplan
     *                         geändert werden soll.
     * @throws AssessException Wenn die Operation mit den angegebenen Parametern unzulässig ist,
     *                         etwa weil z.B. kein Bauplan mit dem angegebenen Namen im System ist.
     */
    public void changePartQuantity(String assembly, String part, int quantityToChangeBy) throws AssessException {
        checkAssemblyExistence(assembly);
        if (quantityToChangeBy == 0) { // passiert im Rahmen meiner Implementierung nicht
            return;
        }
        Map<String, Integer> billOfMaterial = allBoms.get(assembly);
        // Kann null sein, wird dann aber nicht verwendet. Deswegen vom Typ Integer anstatt int.
        Integer currentQuantity = billOfMaterial.get(part);
        if (quantityToChangeBy > 0) {
            if (!billOfMaterial.containsKey(part)) {
                // überprüfen
                checkName(part);
                checkQuantity(quantityToChangeBy);
                Map<String, Integer> bomCopy = new HashMap<>(billOfMaterial); // shallow copy
                bomCopy.put(part, quantityToChangeBy);
                checkContainsSelf(assembly, bomCopy);
                // durchführen
                add(partsUsage, part, 1);
            } else if (currentQuantity + quantityToChangeBy > MAX_QUANTITY) {
                throw new AssessException("the quantity of the specified part in the specified assembly "
                                          + "would exceed the limit of " + MAX_QUANTITY + " by "
                                          + (currentQuantity + quantityToChangeBy - MAX_QUANTITY));
            }
            add(billOfMaterial, part, quantityToChangeBy);
        } else {
            // überprüfen
            if (!billOfMaterial.containsKey(part)) {
                throw new AssessException("the specified assembly does not have a part of the specified name");
            }
            if (currentQuantity + quantityToChangeBy < 0) {
                throw new AssessException("cannot remove more parts than are currently in the assembly ("
                                          + currentQuantity + ")");
            }
            // durchführen
            if (currentQuantity + quantityToChangeBy == 0) {
                billOfMaterial.remove(part);
                updatePartsUsageOnRemoval(part);
                if (billOfMaterial.size() == 0) {
                    removeAssembly(assembly);
                }
            } else {
                billOfMaterial.put(part, currentQuantity + quantityToChangeBy);
            }
        }
    }

    // Prüft, ob die angegebene Quantität gültig ist und wirft eine AssessException, falls nicht.
    private void checkQuantity(int quantity) throws AssessException {
        if (quantity < 1) { // passiert im Rahmen meiner Implementierung nicht
            throw new AssessException("nonpositive quantity or quantities");
        }
        if (quantity > MAX_QUANTITY) {
            throw new AssessException("at least one specified quantity is bigger than the limit of " + MAX_QUANTITY);
        }
    }

    // Prüft, ob der angegebene Name gültig ist und wirft eine AssessException, falls nicht.
    private void checkName(String string) throws AssessException {
        if (!string.matches(ALLOWED_NAMES_RX)) {
            throw new AssessException("invalid name(s), names must match the following pattern: " + ALLOWED_NAMES_RX);
        }
    }

    // Prüft, ob ein Bauplan mit dem angegebenen Namen existiert und wirft eine AssessException, falls nicht.
    private void checkAssemblyExistence(String name) throws AssessException {
        if (!allBoms.containsKey(name)) {
            throw new AssessException("no assembly with this name exists");
        }
    }

    /*
     Ermittelt, ob sich der angegeben Bauplan unter Verwendung der angegebenen Materialstückliste
     selbst enhalten würde und wirft in diesem Fall eine AssessException. Der Pfad ist dann Teil der Fehlermeldung.
     Ausreichend als Zyklencheck, da Rekursion ansonsten nicht entstehen kann.
     */
    private void checkContainsSelf(String assembly, Map<String, Integer> billOfMaterial) throws AssessException {
        Set<String> alreadyChecked = new HashSet<>();
        List<String> assemblyPath = new ArrayList<>();
        assemblyPath.add(assembly);
        if (contains(billOfMaterial, assembly, alreadyChecked, assemblyPath)) {
            throw new AssessException("the assembly you tried to add or modify would contain itself: "
                                      + beautifyRecursivePath(assemblyPath));
        }
    }

    // Gibt zurück ob bomToCheck partToFind enthält, speichert den Pfad in assemblyPath, alreadyChecked optimiert.
    // Nur das erste Attribut ändert sich beim Selbstaufruf.
    private boolean contains(Map<String, Integer> bomToCheck, String partToFind,
                             Set<String> alreadyChecked, List<String> assemblyPath) {
        for (String part: bomToCheck.keySet()) {
            if (alreadyChecked.contains(part)) { // nicht nötig, Optimierung
                continue;
            }
            if (part.equals(partToFind)) {
                return true;
            }
            if (allBoms.containsKey(part)) {
                assemblyPath.add(part);
                if (contains(allBoms.get(part), partToFind, alreadyChecked, assemblyPath)) {
                    return true;
                }
                assemblyPath.remove(part);
            }
            alreadyChecked.add(part);
        }
        return false;
    }

    private String beautifyRecursivePath(List<String> assembliesPath) {
        /* stellt der Praktomat als "??>?" dar
        // \u00a0 = non breaking space, \u2011 = non breaking hyphen, sodass alles in einer Zeile ist wenn möglich
        String arrow =  "\u00a0" + "\u2011" + ">" + "\u00a0"; // " -> " */
        String arrow = " -> ";
        StringBuilder sb = new StringBuilder();
        for (String node: assembliesPath) {
            sb.append(node);
            sb.append(arrow);
        }
        sb.append(assembliesPath.get(0)); // der erste Knoten ist automatisch auch immer der letzte
        return sb.toString();
    }

    private void add(Map<String, Integer> quantityMap, String name, int quantity) {
        quantityMap.put(name, quantityMap.getOrDefault(name, 0) + quantity);
    }

    // mir ist beim besten Willen kein besserer Name eingefallen
    private void updatePartsUsageOnRemoval(String part) {
        partsUsage.put(part, partsUsage.get(part) - 1);
        if (partsUsage.get(part) == 0 && !allBoms.containsKey(part)) {
            partsUsage.remove(part);
        }
    }
}
