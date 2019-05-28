package io.github.essentialsx.itemdbgenerator.providers.alias;

import io.github.essentialsx.itemdbgenerator.providers.item.ItemProvider;
import io.github.essentialsx.itemdbgenerator.providers.item.MaterialEnumProvider;

import java.util.stream.Stream;

public class SimpleAliasProvider implements AliasProvider {

    @Override
    public Stream<String> get(ItemProvider.Item item) {
        Stream.Builder<String> builder = Stream.<String>builder()
            .add(item.getName().replaceAll("_", ""));

        // Only add "minecraft:"-prefixed aliases if the item is direct from the Material enum,
        // as these are the only vanilla items
        if (item instanceof MaterialEnumProvider.MaterialEnumItem) {
            builder.add("minecraft:" + item.getName());
        }

        return builder.build();
    }
}
