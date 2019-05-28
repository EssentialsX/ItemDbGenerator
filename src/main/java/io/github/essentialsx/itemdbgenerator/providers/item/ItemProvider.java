package io.github.essentialsx.itemdbgenerator.providers.item;

import org.bukkit.Material;

import java.util.Objects;
import java.util.stream.Stream;

public interface ItemProvider {

    Stream<Item> get();

    abstract class Item {
        private final Material material;

        public Item(Material material) {
            this.material = material;
        }

        public String getName() {
            return material.name().toLowerCase();
        }

        public Material getMaterial() {
            return material;
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
