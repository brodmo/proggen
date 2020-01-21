package edu.kit.informatik.connectfour.util;

import java.util.Collection;
import java.util.StringJoiner;

public class StringUtil {

    public static String join(Collection<String> strings, String delimiter) {
        StringJoiner joiner = new StringJoiner(delimiter);
        for (String str: strings) {
            joiner.add(str);
        }
        return joiner.toString();
    }
}
