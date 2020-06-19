package io.github.essentialsx.itemdbgenerator.providers.alias;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public abstract class CompoundAliasProvider implements AliasProvider {

    protected Stream<String> getAliases(CompoundModifier type, CompoundType modifier) {
        return Arrays.stream(type.getNames())
                .flatMap(modifier::format);
    }

    interface CompoundModifier {
        String[] getNames();
    }

    interface CompoundType {
        static Pattern generatePattern(final String pattern, final String regex) {
            if (regex == null) {
                return Pattern.compile("[A-Z_]*" + pattern + "[A-Z_]*");
            } else {
                return Pattern.compile(regex);
            }
        }

        static String[] generateFormats(final String typeName, final String... formats) {
            if (formats == null || formats.length == 0) {
                return new String[] {"%s" + typeName.toLowerCase().replaceAll("_", "")};
            }

            return formats;
        }

        String[] getFormats();

        default Stream<String> format(String type) {
            return Arrays.stream(getFormats())
                    .map(format -> String.format(format, type));
        }
    }

}
