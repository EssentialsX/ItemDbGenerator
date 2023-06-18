package net.essentialsx.tools.metagen.vanilla;

import net.essentialsx.tools.metagen.minecraft.ResourceLocation;
import net.essentialsx.tools.metagen.ItemTypeSource;
import net.minecraft.SharedConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.Bootstrap;

import java.util.Set;
import java.util.stream.Collectors;

public class VanillaContentSource implements ItemTypeSource {
    // TODO

    public VanillaContentSource() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        //Bootstrap.validate();
    }

    @Override
    public Set<ResourceLocation> itemTypes() {
        return BuiltInRegistries.ITEM.stream()
                .map(BuiltInRegistries.ITEM::getKey)
                .map(VanillaContentSource::keyFromVanilla)
                .collect(Collectors.toSet());
    }

    private static ResourceLocation keyFromVanilla(net.minecraft.resources.ResourceLocation key) {
        return new ResourceLocation(key.getNamespace(), key.getPath());
    }
}
