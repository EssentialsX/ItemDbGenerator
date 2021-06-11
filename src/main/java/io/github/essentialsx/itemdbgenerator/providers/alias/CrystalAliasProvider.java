package io.github.essentialsx.itemdbgenerator.providers.alias;

import com.google.common.collect.ObjectArrays;
import io.github.essentialsx.itemdbgenerator.providers.item.ItemProvider;
import org.bukkit.Material;

import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CrystalAliasProvider extends CompoundAliasProvider {
    @Override
    public Stream<String> get(ItemProvider.Item item) {
        Crystal crystal = Crystal.of(item.getMaterial());
        CrystalItemType itemType = CrystalItemType.of(item.getMaterial());

        if (crystal == null || itemType == null) return null;

        return getAliases(crystal, itemType);
    }

    /**
     * Represents the different varieties of prismarine.
     */
    @SuppressWarnings("unused")
    private enum Crystal implements CompoundModifier {
        AMETHYST("amethyst", "cave"),
        PRISMARINE_BRICK("prismarinebricks", "prismarinebrick", "prismarinebr", "prisbricks", "prisbrick", "prisbr", "seabricks", "seabrick", "seabr"),
        DARK_PRISMARINE("darkprismarine", "dprismarine", "darkpris", "dpris", "darksea", "dsea"),
        PRISMARINE("prismarine", "pris", "sea");

        private final String[] names;

        Crystal(String... names) {
            this.names = ObjectArrays.concat(name().toLowerCase(), names);
        }

        public static Crystal of(Material material) {
            String matName = material.name();

            for (Crystal crystal : values()) {
                if (matName.contains(crystal.name())) {
                    return crystal;
                }
            }

            return null;
        }

        @Override
        public String[] getNames() {
            return names;
        }
    }

    /**
     * Represents the types of materials with coloured variants.
     */
    @SuppressWarnings("unused")
    private enum CrystalItemType implements CompoundType {
        BRICKS(null, "%s"),
        SLAB(null, "%sslab", "%ssl"),
        STAIRS(null, "%sstairs", "%sstair", "%sst"),
        CRYSTALS(null, "%scrystals", "%scrystal"),
        SHARD(null, "%sshard", "%sfragment"),
        SMALL_BUD("SMALL_[A-Z]+_BUD", "small%sbud", "little%sbud", "s%sbud", "&sbuds"),
        MEDIUM_BUD("MEDIUM_[A-Z]+_BUD", "medium%sbud", "mid%sbud", "m%sbud", "&sbudm"),
        LARGE_BUD("LARGE_[A-Z]+_BUD", "large%sbud", "big%sbud", "l%sbud", "&sbudl"),
        CLUSTER(null, "%scluster", "%sclump", "cluster%s", "clump%s"),
        BLOCK("^((DARK_)?PRISMARINE|[A-Z]+_BLOCK)$", "%s", "%sblock"),
        ;

        private final Pattern regex;
        private final String[] formats;

        CrystalItemType(String regex, String... formats) {
            this.regex = CompoundType.generatePattern(name(), regex);
            this.formats = CompoundType.generateFormats(name(), formats);
        }

        public static CrystalItemType of(Material material) {
            String matName = material.name();

            for (CrystalItemType type : values()) {
                if (type.regex.matcher(matName).matches()) {
                    return type;
                }
            }

            return null;
        }

        @Override
        public String[] getFormats() {
            return formats;
        }
    }

}
