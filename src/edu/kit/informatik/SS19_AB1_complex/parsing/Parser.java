package edu.kit.informatik.SS19_AB1_complex.parsing;

import edu.kit.informatik.SS19_AB1_complex.MagicStrings;
import edu.kit.informatik.SS19_AB1_complex.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

public enum Parser {
    EXPRESSION {
        @Override
        ComplexNumber tryParsing(String string, VariableManager vm) throws ParseException {
            Parser[] delegates = {BRACKET, LITERAL, ADDITION_SUBTRACTION, MULTIPLICATION_DIVISION};
            // important: ADDITION_SUBTRACTION before MULTIPLICATION_DIVISION for correct precedence
            return delegate(string, vm, delegates);
        }
    },
    LITERAL {
        @Override
        ComplexNumber tryParsing(String string, VariableManager vm) throws ParseException {
            Parser[] delegates = {VARIABLE, COMPLEX};
            return delegate(string, vm, delegates);
        }
    },
    COMPLEX {
        @Override
        ComplexNumber tryParsing(String string, VariableManager vm) throws ParseException {
            if (!COMPLEX_PATTERN.matcher(string).matches()) {
                return null;
            }
            String[] parts = string.split(Pattern.quote("" + MagicStrings.ADD));
            String realPart = parts[0].substring(1, parts[0].length() - 1);
            String imaginaryPart = parts[1].substring(1, parts[1].length() - 2);
            try {
                return new ComplexNumber(Integer.parseInt(realPart), Integer.parseInt(imaginaryPart));
            } catch (NumberFormatException e) {
                throw new ParseException("at least one number too big/small");
            }
        }
    },
    ADDITION_SUBTRACTION {
        @Override
        ComplexNumber tryParsing(String string, VariableManager vm) throws ParseException {
            Map<Character, BiFunction<ComplexNumber, ComplexNumber, ComplexNumber>> operations
                    = new HashMap<>();
            operations.put(MagicStrings.ADD, ComplexCalculator::add);
            operations.put(MagicStrings.SUBTRACT, ComplexCalculator::subtract);
            return tryParsingArithmeticOperations(string, vm, operations);
        }
    },
    MULTIPLICATION_DIVISION {
        @Override
        ComplexNumber tryParsing(String string, VariableManager vm) throws ParseException {
            Map<Character, BiFunction<ComplexNumber, ComplexNumber, ComplexNumber>> operations
                    = new HashMap<>();
            operations.put(MagicStrings.MULTIPLY, ComplexCalculator::multiply);
            operations.put(MagicStrings.DIVIDE, ComplexCalculator::divide);
            return tryParsingArithmeticOperations(string, vm, operations);
        }
    },
    BRACKET {
        @Override
        ComplexNumber tryParsing(String string, VariableManager vm) throws ParseException {
            if (string.length() <= 2 || string.charAt(0) != MagicStrings.OPEN_BRACKET
                    || string.charAt(string.length() - 1) != MagicStrings.CLOSED_BRACKET) {
                return null; // if string is not in brackets
            }
            String bracketLessString = string.substring(1, string.length() - 1);
            int counter = 0;
            for (char c : bracketLessString.toCharArray()) {
                if (c == MagicStrings.CLOSED_BRACKET) {
                    counter--;
                } else if (c == MagicStrings.OPEN_BRACKET) {
                    counter++;
                }
                if (counter < 0) {
                // doesn't necessarily indicate a bracket error, see for example [a] + [b] (when a and b don't exist)
                    return null;
                }
            }
            return EXPRESSION.tryParsing(bracketLessString, vm);
        }
    },
    VARIABLE  {
        @Override
        ComplexNumber tryParsing(String string, VariableManager vm) {
            if (!vm.hasVariable(string)) {
                return null;
            }
            return vm.getVariable(string);
        }
    };

    private static final String INTEGER_RX = "[+-]?[0-9]+";

    private static final Pattern COMPLEX_PATTERN = Pattern
                    .compile(Pattern.quote("" + MagicStrings.COMPLEX_OPEN_BRACKET) + INTEGER_RX
                    + Pattern.quote("" + MagicStrings.SPACE + MagicStrings.ADD + MagicStrings.SPACE) + INTEGER_RX
                    + Pattern.quote("" + MagicStrings.COMPLEX_SYMBOL + MagicStrings.COMPLEX_CLOSED_BRACKET));

    abstract ComplexNumber tryParsing(String string, VariableManager vm) throws ParseException;

    private static ComplexNumber delegate(String string, VariableManager vm, Parser[] delegates)
            throws ParseException {
        ComplexNumber parsed;
        for (Parser parser : delegates) {
            parsed = parser.tryParsing(string, vm);
            if (parsed != null) {
                return parsed;
            }
        }
        return null;
    }

    private static ComplexNumber tryParsingArithmeticOperations(String string, VariableManager vm,
            Map<Character, BiFunction<ComplexNumber, ComplexNumber, ComplexNumber>> operations)
            throws ParseException {
        int brackets = 0;
        int complexBrackets = 0;
        ComplexNumber parsed;
        char c;
        for (int index = string.length() - 1; index >= 0; index--) { // iterate backwards for left to right precedence
            c = string.charAt(index);
            switch (c) {
                case MagicStrings.SPACE:
                    if (index < string.length() - 2 && string.charAt(index + 1) == MagicStrings.SPACE) {
                        throw new ParseException("two or more spaces next to each other");
                    }
                    break;
                case MagicStrings.OPEN_BRACKET:
                    brackets--; // wrong way around because we're iterating backwards through the string
                    break;
                case MagicStrings.CLOSED_BRACKET:
                    brackets++;
                    break;
                case MagicStrings.COMPLEX_OPEN_BRACKET:
                    complexBrackets--;
                    break;
                case MagicStrings.COMPLEX_CLOSED_BRACKET:
                    complexBrackets++;
                    break;
                default:
                    if (brackets == 0 && complexBrackets == 0 && operations.keySet().contains(c)) {
                        parsed = tryUsingOperationAtIndex(string, vm, index, operations.get(c));
                        if (parsed != null) {
                            return parsed;
                        }
                    }
                    break;
            }
            if (brackets < 0 || complexBrackets < 0) {
                throw new ParseException("invalid brackets");
            }
        }
        if (brackets != 0 || complexBrackets != 0) {
            throw new ParseException("invalid brackets");
        }
        return null;
    }

    private static ComplexNumber tryUsingOperationAtIndex(String string, VariableManager vm, int index,
            BiFunction<ComplexNumber, ComplexNumber, ComplexNumber> operation) throws ParseException {
        // operators at first and last index not detected, but they're not permitted anyways
        if (indexInSpaces(string, index)) {
            ComplexNumber parsedBefore = EXPRESSION.tryParsing(string.substring(0, index - 1), vm);
            // index + 2 not a problem because "123".substring(3) ist just ""
            ComplexNumber parsedAfter = EXPRESSION.tryParsing(string.substring(index + 2), vm);
            if (parsedBefore != null && parsedAfter != null) {
                return operation.apply(parsedBefore, parsedAfter);
            }
        }
        return null;
    }

    private static boolean indexInSpaces(String string, int index) {
        return index < string.length() - 1 && string.charAt(index + 1) == MagicStrings.SPACE
                && index > 0 && string.charAt(index - 1) == MagicStrings.SPACE;
    }

    public static ComplexNumber parseExpression(String string, VariableManager vm) throws ParseException {
        ComplexNumber parsed = EXPRESSION.tryParsing(string, vm);
        if (parsed == null) {
            throw new ParseException();
        }
        return parsed;
    }
}
