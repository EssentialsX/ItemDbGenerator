package net.essentialsx.itemaliasgenerator.providers.alias;

import net.essentialsx.itemaliasgenerator.providers.item.ItemProvider;

import java.util.stream.Stream;

public interface AliasProvider {

    Stream<String> get(ItemProvider.Item item);

}
