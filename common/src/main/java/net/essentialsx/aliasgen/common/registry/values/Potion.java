package net.essentialsx.aliasgen.common.registry.values;

import net.essentialsx.aliasgen.common.util.Keyed;
import net.kyori.adventure.key.Key;

public record Potion(Key key) implements Keyed {

    public Modifier modifier() {
        if (value().contains("long_")) {
            return Modifier.LONG;
        } else if (value().contains("strong_")) {
            return Modifier.STRONG;
        }

        return Modifier.NONE;
    }

    public enum Modifier {
        NONE,
        LONG,
        STRONG,
    }

}
