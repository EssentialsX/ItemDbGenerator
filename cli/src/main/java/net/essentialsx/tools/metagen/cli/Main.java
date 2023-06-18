package net.essentialsx.tools.metagen.cli;

import net.essentialsx.tools.metagen.ItemTypeSource;
import net.essentialsx.tools.metagen.minecraft.ComplexItem;
import net.essentialsx.tools.metagen.vanilla.VanillaContentSource;

import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        ItemTypeSource itemTypeSource = new VanillaContentSource();

        ComplexItem.Source complexItemSource = () -> itemTypeSource.itemTypes().stream()
                .map(ComplexItem::new)
                .collect(Collectors.toSet());

        complexItemSource.complexItems().forEach(System.out::println);
    }
}
