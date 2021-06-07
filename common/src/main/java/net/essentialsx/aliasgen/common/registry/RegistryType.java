package net.essentialsx.aliasgen.common.registry;

import net.essentialsx.aliasgen.common.registry.values.Item;
import net.essentialsx.aliasgen.common.registry.values.Potion;
import net.essentialsx.aliasgen.common.util.Keyed;
import net.kyori.adventure.key.Key;

public class RegistryType<E> implements Keyed {

    public static final RegistryType<Item> ITEM = RegistryType.of(Key.MINECRAFT_NAMESPACE, "item");
    public static final RegistryType<Potion> POTION = RegistryType.of(Key.MINECRAFT_NAMESPACE, "potion");

    private final Key key;

    private RegistryType(Key key) {
        this.key = key;
    }

    private static <E> RegistryType<E> of(final String namespace, final String value) {
        return new RegistryType<>(Key.key(namespace, value));
    }

    @Override
    public Key key() {
        return key;
    }
}
