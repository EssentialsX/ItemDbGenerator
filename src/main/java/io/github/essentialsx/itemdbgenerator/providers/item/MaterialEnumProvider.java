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
                .filter(mat -> {
                    if (Main.VALID_ITEMS != null) {
                        String id = mat.name().toLowerCase();
                        return Main.VALID_ITEMS.contains(id) || Main.VALID_ITEMS.contains("minecraft:" + id);
                    }
                    return mat.isItem();
                })
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
