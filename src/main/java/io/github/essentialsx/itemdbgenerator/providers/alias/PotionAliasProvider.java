package io.github.essentialsx.itemdbgenerator.providers.alias;

import io.github.essentialsx.itemdbgenerator.providers.item.ItemProvider;
import io.github.essentialsx.itemdbgenerator.providers.item.PotionProvider;
import java.util.stream.Stream;

public class PotionAliasProvider implements AliasProvider {
    @Override
    public Stream<String> get(ItemProvider.Item item) {
        if (!(item instanceof PotionProvider.PotionItem)) return null;

        // TODO: stuff
        return null;
    }
}
