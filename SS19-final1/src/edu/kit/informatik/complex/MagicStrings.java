package edu.kit.informatik.complex;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MagicStrings {

    public static final char SPACE = ' ';

    public static final char OPEN_BRACKET = '[';

    public static final char CLOSED_BRACKET = ']';

    public static final char COMPLEX_OPEN_BRACKET = '(';

    public static final char COMPLEX_CLOSED_BRACKET = ')';

    public static final char COMPLEX_SYMBOL = 'i';

    public static final char ASSIGNMENT = '=';

    public static final char ADD = '+';

    public static final char SUBTRACT = '-';

    public static final char MULTIPLY = '*';

    public static final char DIVIDE = '/';

    public static final String QUIT = "quit";

    public static final Set<String> JAVA_KEYWORDS = new HashSet<>(Arrays.asList(
            "abstract",
            "continue",
            "for",
            "new",
            "switch",
            "assert",
            "default",
            "goto",
            "package",
            "synchronized",
            "boolean",
            "do",
            "if",
            "private",
            "this",
            "break",
            "double",
            "implements",
            "protected",
            "throw",
            "byte",
            "else",
            "import",
            "public",
            "throws",
            "case",
            "enum",
            "instanceof",
            "return",
            "transient",
            "catch",
            "extends",
            "int",
            "short",
            "try",
            "char",
            "final",
            "interface",
            "static",
            "void",
            "class",
            "finally",
            "long",
            "strictfp",
            "volatile",
            "const",
            "float",
            "native",
            "super",
            "while",
            "true",
            "false",
            "null"
    ));

}
