package edu.kit.informatik.complex.IO;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.complex.MagicStrings;
import edu.kit.informatik.complex.model.ComplexNumber;
import edu.kit.informatik.complex.model.DivideByZeroException;
import edu.kit.informatik.complex.model.VariableManager;
import edu.kit.informatik.complex.parsing.ParseException;
import edu.kit.informatik.complex.parsing.Parser;

public class Main {

    // SS19 Abschluss 1
    public static void main(String[] args) {
        VariableManager vm = new VariableManager();
        String input = Terminal.readLine();
        String[] parts;
        while (!input.equals(MagicStrings.QUIT)) {
            try {
                parts = input.split("" + MagicStrings.SPACE + MagicStrings.ASSIGNMENT + MagicStrings.SPACE, -1);
                if (parts.length == 1) {
                    if (!vm.hasVariable(parts[0])) {
                        throw new InvalidInputException("no variable of this name");
                    }
                } else if (parts.length == 2) {
                    assignVariable(vm, parts[0], parts[1]);
                } else {
                    throw new InvalidInputException("more than one "
                            + Character.getName(MagicStrings.ASSIGNMENT).toLowerCase());
                }
                Terminal.printLine(parts[0] + " " + MagicStrings.ASSIGNMENT + " " + vm.getVariable(parts[0]));
            } catch (InvalidInputException e) {
                Terminal.printError("invalid input: " + e.getMessage());
            } catch (ParseException e) {
                String messageBegin = "couldn't parse complex number";
                if (e.getMessage() == null) {
                    Terminal.printError(messageBegin);
                } else {
                    Terminal.printError(messageBegin + ": " + e.getMessage());
                }
            } catch (DivideByZeroException e) {
                Terminal.printError("cannot divide by zero");
            }
            input = Terminal.readLine();
        }
    }

    private static void assignVariable(VariableManager vm, String variable, String expression)
            throws InvalidInputException, ParseException {
        if (!isValidVariableName(variable) || variable.equals(MagicStrings.QUIT)) {
            throw new InvalidInputException("invalid variable name");
        }
        ComplexNumber parsedNumber = Parser.parseExpression(expression, vm);
        vm.setVariable(variable, parsedNumber);
    }

    private static boolean isValidVariableName(String string) {
        if (MagicStrings.JAVA_KEYWORDS.contains(string) || string.isEmpty()
            || !Character.isJavaIdentifierStart(string.charAt(0))) {
            return false;
        }
        for (char c : string.toCharArray()) {
            if (!Character.isJavaIdentifierPart(c)) {
                return false;
            }
        }
        return true;
    }
}
