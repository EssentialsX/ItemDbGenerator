package net.essentialsx.aliasgen.common.util;

import net.kyori.adventure.key.Key;

public interface Keyed {

    Key key();

    default String namespace() {
        return key().namespace();
    }

    default String value() {
        return key().value();
    }

}
