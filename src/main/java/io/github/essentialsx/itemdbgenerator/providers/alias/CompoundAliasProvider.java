package io.github.essentialsx.itemdbgenerator.providers.alias;

import java.util.regex.Pattern;

public abstract class CompoundAliasProvider implements AliasProvider {

    static Pattern getTypePattern(final String typeName, final String regex) {
        if (regex == null) {
            return Pattern.compile("[A-Z_]*" + typeName + "[A-Z_]*");
        } else {
            return Pattern.compile(regex);
        }
    }

    static String[] getTypeFormats(final String typeName, final String... formats) {
        if (formats == null || formats.length == 0) {
            return new String[] {"%s" + typeName.toLowerCase().replaceAll("_", "")};
        }

        return formats;
    }

}
