package io.github.essentialsx.itemdbgenerator.providers.item;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpawnerProviderTest {

    private Stream<ItemProvider.Item> stream;

    @BeforeEach
    void getStream() {
        stream = (new SpawnerProvider()).get();
    }

    @Test
    void testNames() {
        Set<String> names = stream
            .map(ItemProvider.Item::getName)
            .collect(Collectors.toSet());

        assertTrue(names.contains("pig_spawner"), "missing pig spawner");
        assertFalse(names.contains("_spawner"), "spawner without entity");
        assertFalse(names.contains("firework_spawner"), "non-spawnable entity");
        assertFalse(names.contains("area_effect_cloud_spawner"), "non-spawnable entity");
    }

}