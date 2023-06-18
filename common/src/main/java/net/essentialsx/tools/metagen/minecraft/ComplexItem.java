package net.essentialsx.tools.metagen.minecraft;

import java.util.Set;

// TODO: potion effects etc.
public record ComplexItem(ResourceLocation type) {
    public interface Source {
        Set<ComplexItem> complexItems();
    }
}
