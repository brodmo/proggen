package edu.kit.informatik.materialassessment.ui;

import edu.kit.informatik.materialassessment.model.AssessException;
import edu.kit.informatik.materialassessment.model.Assessor;
import edu.kit.informatik.materialassessment.model.PartBatch;

import static edu.kit.informatik.materialassessment.ui.Utility.splitInTwo;

import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;


public enum Command {
    ADD_ASSEMBLY("addAssembly") {
        public String execute(Assessor a, String argument) throws ParseException, AssessException {
            addAssembly(a, argument);
            return OK;
        }
    },
    REMOVE_ASSEMBLY("removeAssembly") {
        public String execute(Assessor a, String argument) throws AssessException {
            a.removeAssembly(argument);
            return OK;
        }
    },
    PRINT_ASSEMBLY("printAssembly") {
        public String execute(Assessor a, String argument) throws AssessException {
            return bomToString(a.getBom(argument));
        }
    },
    GET_ASSEMBLIES("getAssemblies") {
        public String execute(Assessor a, String argument) throws AssessException {
            return partBatchesToString(a.getPartBatches(argument, true));
        }
    },
    GET_COMPONENTS("getComponents") {
        public String execute(Assessor a, String argument) throws AssessException {
            return partBatchesToString(a.getPartBatches(argument, false));
        }
    },
    ADD_PART("addPart") {
        public String execute(Assessor a, String argument) throws ParseException, AssessException {
            changePartQuantity(a, argument, true);
            return OK;
        }
    },
    REMOVE_PART("removePart") {
        public String execute(Assessor a, String argument) throws ParseException, AssessException {
            changePartQuantity(a, argument, false);
            return OK;
        }
    },
    UNKNOWN("unknown") {
        public String execute(Assessor a, String argument) throws ParseException {
            throw new ParseException("unknown command");
        }
    };

    /**
     * Führt den Befehl aus und gibt sämtliche Ausgaben aus.
     * @param a Der Ermittler, auf dem der Befehl ausgeführt werden soll.
     * @param argument Der Parameter des Befehls.
     * @return Der Output, den der Befehl erzeugt.
     */
    public abstract String execute(Assessor a, String argument) throws ParseException, AssessException;

    /**
     * Das/die Zeichen, das/die verwendet werden soll(en), um Quantität und Teilname voneinander zu trennen.
     */
    private static final String QUANTITY_NAME_SEPARATOR = ":";
    /**
     * Das/die Zeichen, das/die verwendet werden soll(en), um verschiedene Teilposten voneinander zu trennen.
     */
    private static final String PART_BATCHES_SEPARATOR = ";";

    private static final String OK = "OK";

    private static void addAssembly(Assessor a, String argument) throws ParseException, AssessException {
        // Eigentlich ist = das einzige Zeichen, das an dieser Stelle Sinn macht.
        // Kann dennoch ohne viel Aufwand geändert werden, da es nur in dieser einen Zeile vorkommt.
        String[] parts = splitInTwo(argument, "=");
        String assemblyName = parts[0];
        SortedMap<String, Integer> billOfMaterial = new TreeMap<>();
        String[] ptbParts;
        int partQuantity;
        String partName;
        String[] partBatches = parts[1].split(PART_BATCHES_SEPARATOR, -1);
        for (String ptb: partBatches) {
            try {
                ptbParts = splitInTwo(ptb, QUANTITY_NAME_SEPARATOR);
            } catch (ParseException e) {
                String msg = e.getMessage();
                throw new ParseException(msg.substring(0, msg.length() - ", expected one".length())
                        + " within at least one part batch, expected one");
            }
            partQuantity = parseQuantity(ptbParts[0]);
            partName = ptbParts[1];
            if (billOfMaterial.containsKey(partName)) {
                throw new ParseException("each part may appear only once, but " + partName
                        + " appears at least twice");
            }
            billOfMaterial.put(partName, partQuantity);
        }
        a.addAssembly(assemblyName, billOfMaterial);
    }

    // boolean flag für add. true bedeutet add, false bedeutet remove.
    private static void changePartQuantity(Assessor a, String argument, boolean add) throws ParseException, AssessException {
        String[] parts;
        if (add) {
            // Eigentlich sind + und -  die einzigen Zeichen, die an dieser Stelle Sinn machen.
            // Können dennoch ohne viel Aufwand geändert werden, da sie nur hier in jeweils einer Zeile vorkommen.
            parts = splitInTwo(argument, "+");
        } else {
            parts = splitInTwo(argument, "-");
        }
        String assemblyName = parts[0];
        String partBatch = parts[1];
        String[] subParts = splitInTwo(partBatch, QUANTITY_NAME_SEPARATOR);
        int quantity = parseQuantity(subParts[0]);
        String partName = subParts[1];
        if (add) {
            a.changePartQuantity(assemblyName, partName, quantity);
        } else {
            a.changePartQuantity(assemblyName, partName, -quantity);
        }
    }

    // gibt das Gewünschte nicht direkt aus, damit alle Ausgaben möglichst nah beinander sind
    private static String partBatchesToString(PartBatch[] partBatches) {
        if (partBatches.length == 0) {
            return "EMPTY";
        }
        StringBuilder sb = new StringBuilder(partBatches.length * 4);
        for (PartBatch ptb: partBatches) {
            appendPart(sb, ptb.name(), ptb.quantity());
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    // gibt das Gewünschte nicht direkt aus, damit alle Ausgaben im Code möglichst nah beieinander sind
    private static String bomToString(SortedMap<String, Integer> billOfMaterial) {
        if (billOfMaterial.isEmpty()) {
            return "COMPONENT";
        }
        Set<String> parts = billOfMaterial.keySet();
        StringBuilder sb = new StringBuilder(parts.size() * 4);
        for (String pt: parts) {
            appendPart(sb, pt, billOfMaterial.get(pt));
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private static void appendPart(StringBuilder sb, String name, long quantity) {
        sb.append(name);
        sb.append(QUANTITY_NAME_SEPARATOR);
        sb.append(quantity);
        sb.append(PART_BATCHES_SEPARATOR);
    }

    /**
     * Überprüft, ob der angegeben String eine gültige Mengenangabe darstellt und gibt sie als int zurück falls ja.
     * Wirft eine ParseException falls nicht.
     * @param string Der String, den es zu überprüfen gilt.
     * @return Die Mengenangabe als int.
     * @throws ParseException Wenn der String keine gültige Mengenangabe darstellt.
     */
    private static int parseQuantity(String string) throws ParseException {
        if (!string.matches("[1-9][0-9]*")) {
            throw new ParseException("invalid quantity or quantities");
        }
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            throw new ParseException("far too big quantity or quantities");
        }
    }

    private String string;

    Command(String input) {
        this.string = input;
    }

    public static Command matchCommand(String input) {
        for (Command cmd: Command.values()) {
            if (input.equals(cmd.string)) {
                return cmd;
            }
        }
        return UNKNOWN;
    }
}
