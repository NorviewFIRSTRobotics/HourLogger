package team1793.utils;

import java.util.Arrays;
import java.util.function.Function;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 10/14/16
 */
public class StringUtil {

    public static String concat(char spacer, StringFormat format, String... strings) {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(strings).forEach(s -> { builder.append(format.apply(s)); builder.append(spacer);});
        return builder.toString().trim();
    }

    public static String capitalize(String name) {
        if (name != null && name.length() != 0) {
            char[] chars = name.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            return new String(chars);
        } else {
            return name;
        }
    }
    public enum StringFormat implements Function<String, String> {
        LOWERCASE(String::toLowerCase),
        UPPERCASE(String::toUpperCase),
        CAPITALIZED(StringUtil::capitalize);
        private Function<String,String> func;
        StringFormat(Function<String,String> func) {
            this.func = func;
        }
        @Override
        public String apply(String s) {
            return func.apply(s);
        }
    }
}
