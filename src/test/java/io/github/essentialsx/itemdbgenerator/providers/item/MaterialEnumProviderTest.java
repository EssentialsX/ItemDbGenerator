package io.github.essentialsx.itemdbgenerator.providers.item;

import org.bukkit.Material;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class MaterialEnumProviderTest {

    private Stream<ItemProvider.Item> stream;

    @BeforeEach
    void getStream() {
        stream = (new MaterialEnumProvider()).get();
    }

    @Test
    void testNames() {
        Set<String> names = stream
            .map(ItemProvider.Item::getName)
            .collect(Collectors.toSet());

        Assertions.assertTrue(names.contains("cobblestone"), "missing cobblestone");
        Assertions.assertTrue(names.contains("coarse_dirt"), "missing underscore names");
        Assertions.assertFalse(names.contains("legacy_cobblestone"), "legacy items");
        Assertions.assertFalse(names.contains("coarsedirt"), "underscore-less names");
        Assertions.assertFalse(names.contains("minecraft:dirt"), "namespaced names");
    }

    @Test
    void testMaterials() {
        Set<Material> materials = stream
            .map(ItemProvider.Item::getMaterial)
            .collect(Collectors.toSet());

        Assertions.assertTrue(materials.contains(Material.DIRT), "missing dirt");
        Assertions.assertFalse(materials.contains(Material.LEGACY_DIRT), "legacy items");
        Assertions.assertFalse(materials.contains(Material.WALL_TORCH), "non-item materials");
    }
}