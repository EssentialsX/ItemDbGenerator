package io.github.essentialsx.itemdbgenerator.providers.alias;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public abstract class CompoundAliasProvider implements AliasProvider {

    private final Set<String> skippedAliases;

    protected CompoundAliasProvider() {
        this(Collections.emptySet());
    }

    protected CompoundAliasProvider(Set<String> skippedAliases) {
        this.skippedAliases = skippedAliases;
    }

    protected Stream<String> getAliases(CompoundModifier type, CompoundType modifier) {
        return Arrays.stream(type.getNames())
                .flatMap(modifier::format)
                .filter(alias -> !isAliasSkipped(alias));
    }

    private boolean isAliasSkipped(String alias) {
        return this.skippedAliases.contains(alias);
    }

    interface CompoundModifier {
        String[] getNames();

        String name();
    }

    interface MultipleCompoundModifier extends CompoundModifier {
        default String[] generateNames(String modifier, CompoundModifier[] innerModifiers, String... names) {
            List<String> newNames = new ArrayList<>();
            newNames.add(modifier.toLowerCase().replace("_", ""));

            for (String name : names) {
                List<String> workingNames = new ArrayList<>();
                workingNames.add(name);
                for (CompoundModifier innerModifier : innerModifiers) {
                    String placeholder = "{" + innerModifier.name() + "}";

                    for (String workingName : new ArrayList<>(workingNames)) {
                        if (workingName.contains(placeholder)) {
                            workingNames.remove(workingName);
                            for (String replacement : innerModifier.getNames()) {
                                workingNames.add(workingName.replace(placeholder, replacement));
                            }
                        }
                    }
                }
                newNames.addAll(workingNames);
            }
            return newNames.toArray(new String[0]);
        }
    }

    interface CompoundType {
        static Pattern generatePattern(final String typeName, final String regex) {
            if (regex == null) {
                return Pattern.compile("[A-Z_]*" + typeName + "[A-Z_]*");
            } else {
                return Pattern.compile(regex);
            }
        }

        static String[] generateFormats(final String typeName, final String... formats) {
            if (formats == null || formats.length == 0) {
                return new String[]{"%s" + typeName.toLowerCase().replace("_", "")};
            }

            return formats;
        }

        String[] getFormats();

        default Stream<String> format(String type) {
            return Arrays.stream(getFormats())
                    .map(format -> String.format(format, type));
        }
    }

    protected static class SingleCompoundType implements CompoundType {
        private final Pattern pattern;
        private final String[] formats;

        public SingleCompoundType(String regex, String... formats) {
            this.pattern = Pattern.compile(regex);
            this.formats = formats;
        }

        @Override
        public String[] getFormats() {
            return formats;
        }

        public boolean matches(Material material) {
            return pattern.matcher(material.name()).matches();
        }
    }

}
