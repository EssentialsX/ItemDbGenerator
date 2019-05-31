package io.github.essentialsx.itemdbgenerator.providers.item;

import org.bukkit.Material;

import java.util.Objects;
import java.util.stream.Stream;

public interface ItemProvider {

    Stream<Item> get();

    abstract class Item {
        private final Material material;
        private final String[] fallbacks;

        public Item(Material material) {
            this.material = material;
            this.fallbacks = MaterialFallbacks.get(material);
        }

        public String getName() {
            return material.name().toLowerCase();
        }

        public Material getMaterial() {
            return material;
        }

        public String[] getFallbacks() {
            return fallbacks;
        }

        @Override
        public int hashCode() {
            return Objects.hash(material);
        }

        @Override
        public boolean equals(Object obj) {
            return hashCode() == obj.hashCode();
        }
    }
}
