package io.github.essentialsx.itemdbgenerator.providers.item;

import io.github.essentialsx.itemdbgenerator.Main;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Provides items from the Material enum.
 */
public class MaterialEnumProvider implements ItemProvider {
    @Override
    public Stream<Item> get() {
        return Arrays.stream(Material.values())
                .filter(mat -> !mat.name().contains("LEGACY"))
                .filter(mat -> !Main.EXPERIMENTAL_MATERIALS.contains(mat))
                .filter(Material::isItem)
                .map(MaterialEnumItem::new);
    }

    public static class MaterialEnumItem extends Item {
        private MaterialEnumItem(Material material) {
            super(material);
        }

        public String getName() {
            return getMaterial().name().toLowerCase();
        }
    }
}
