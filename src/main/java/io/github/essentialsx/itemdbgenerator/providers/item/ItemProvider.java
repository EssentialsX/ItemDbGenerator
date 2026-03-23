package io.github.essentialsx.itemdbgenerator.providers.item;

import com.google.gson.annotations.SerializedName;
import org.bukkit.Material;

import java.util.Objects;
import java.util.stream.Stream;

public interface ItemProvider {

    Stream<Item> get();

    abstract class Item implements Comparable {
        private transient final Material material;
        @SerializedName("material")
        private final String materialName;
        private final String[] fallbacks;

        public Item(Material material) {
            this.material = material;
            this.materialName = material.name();
            this.fallbacks = MaterialFallbacks.get(material);
        }

        public Item(String registryName) {
            this.material = null;
            this.materialName = registryName.toUpperCase();
            this.fallbacks = null;
        }

        public abstract String getName();

        public Material getMaterial() {
            return material;
        }

        public String getMaterialName() {
            return materialName;
        }

        public String[] getFallbacks() {
            return fallbacks;
        }

        @Override
        public int hashCode() {
            return Objects.hash(materialName);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Item)) return false;
            return hashCode() == obj.hashCode();
        }

        @Override
        public int compareTo(Object obj) {
            Item item = (Item) obj;
            return getName().compareToIgnoreCase(item.getName());
        }
    }
}
