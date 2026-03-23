package io.github.essentialsx.itemdbgenerator.providers.item;

import io.github.essentialsx.itemdbgenerator.Main;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides items from the Minecraft registry that don't have a matching
 * Paper Material enum entry. This ensures new items are included even
 * when the Paper API hasn't been updated yet.
 */
public class RegistryItemProvider implements ItemProvider {

    @Override
    public Stream<Item> get() {
        if (Main.VALID_ITEMS == null) {
            return Stream.empty();
        }

        Set<String> knownMaterials = Arrays.stream(Material.values())
                .filter(mat -> !mat.name().contains("LEGACY"))
                .map(mat -> mat.name().toLowerCase())
                .collect(Collectors.toSet());

        return Main.VALID_ITEMS.stream()
                .filter(name -> !knownMaterials.contains(name))
                .map(RegistryItem::new);
    }

    public static class RegistryItem extends Item {
        private final String name;

        private RegistryItem(String registryName) {
            super(registryName);
            this.name = registryName;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
