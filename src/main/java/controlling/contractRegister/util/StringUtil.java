package controlling.contractRegister.util;

public class StringUtil {

    public static String replaceEmptyStringWithNull(String input) {
        return input == null || input.trim().equals("") ? null : input;
    }

    public static String replaceNullWithEmptyString(String input) {
        return input == null ? "" : input;
    }
}