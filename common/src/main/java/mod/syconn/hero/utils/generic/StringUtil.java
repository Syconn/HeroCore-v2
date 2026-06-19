package mod.syconn.hero.utils.generic;

import net.minecraft.client.KeyMapping;

public class StringUtil {

    public static String trimPrefix(String str, String prefix) {
        if (str.startsWith(prefix)) return str.substring(prefix.length());
        return str;
    }

    public static String trimSuffix(String str, String suffix) {
        if (str.endsWith(suffix)) return str.substring(0, str.length() - suffix.length());
        return str;
    }

    public static String keyToString(KeyMapping mapping) {
        var split = mapping.saveString().split("\\.");
        var string = split[2].toUpperCase();
        return split.length > 3 ? string + " " + split[3].toUpperCase() : string;
    }
}
