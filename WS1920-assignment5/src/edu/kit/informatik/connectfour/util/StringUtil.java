package edu.kit.informatik.connectfour.util;

import java.util.Collection;
import java.util.StringJoiner;

public class StringUtil {

    public static final String DELIMITER = " ";

    public static String join(Collection<String> strings) {
        StringJoiner joiner = new StringJoiner(DELIMITER);
        for (String str: strings) {
            joiner.add(str);
        }
        return joiner.toString();
    }
}
