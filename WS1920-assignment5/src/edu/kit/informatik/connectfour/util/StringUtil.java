package edu.kit.informatik.connectfour.util;

import java.util.StringJoiner;

public final class StringUtil {

    public static final String DELIMITER = " ";

    public static String join(Iterable<String> strings) {
        StringJoiner joiner = new StringJoiner(DELIMITER);
        for (String str: strings) {
            joiner.add(str);
        }
        return joiner.toString();
    }
}
