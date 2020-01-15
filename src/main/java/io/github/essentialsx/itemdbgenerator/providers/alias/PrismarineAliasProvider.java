package io.github.essentialsx.itemdbgenerator.providers.alias;

import com.google.common.collect.ObjectArrays;
import io.github.essentialsx.itemdbgenerator.providers.item.ItemProvider;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.bukkit.Material;

public class PrismarineAliasProvider extends CompoundAliasProvider {
    @Override
    public Stream<String> get(ItemProvider.Item item) {
        PrismarineType prismarineType = PrismarineType.of(item.getMaterial());
        PrismarineItemType itemType = PrismarineItemType.of(item.getMaterial());

        if (prismarineType == null || itemType == null) return null;

        return getAliases(prismarineType, itemType);
    }

    private Stream<String> getAliases(PrismarineType prismarineType, PrismarineItemType itemType) {
        return Arrays.stream(prismarineType.names)
            .flatMap(itemType::format);
    }



    /**
     * Represents the different varieties of prismarine.
     */
    @SuppressWarnings("unused")
    private enum PrismarineType {
        PRISMARINE_BRICK("prismarinebricks", "prismarinebrick", "prismarinebr", "prisbricks", "prisbrick", "prisbr", "seabricks", "seabrick", "seabr"),
        DARK_PRISMARINE("darkprismarine", "dprismarine", "darkpris", "dpris", "darksea", "dsea"),
        PRISMARINE("prismarine", "pris", "sea")
        ;

        private final String[] names;

        PrismarineType(String... names) {
            this.names = ObjectArrays.concat(name().toLowerCase(), names);
        }

        public static PrismarineType of(Material material) {
            String matName = material.name();

            for (PrismarineType prismarineType : values()) {
                if (matName.contains(prismarineType.name())) {
                    return prismarineType;
                }
            }

            return null;
        }
    }

    /**
     * Represents the types of materials with coloured variants.
     */
    @SuppressWarnings("unused")
    private enum PrismarineItemType {
        BRICKS(null, "%s"),
        SLAB(null, "%sslab", "%ssl"),
        STAIRS(null, "%sstairs", "%sstair", "%sst"),
        CRYSTALS(null, "%scrystals", "%scrystal"),
        SHARD(null, "%sshard", "%sfragment"),
        BLOCK("^(DARK_)?PRISMARINE$", "%s", "%sblock"),
        ;

        private final Pattern regex;
        private final String[] formats;

        PrismarineItemType(String regex, String... formats) {
            this.regex = getTypePattern(name(), regex);
            this.formats = getTypeFormats(name(), formats);
        }

        public static PrismarineItemType of(Material material) {
            String matName = material.name();

            for (PrismarineItemType type : values()) {
                if (type.regex.matcher(matName).matches()) {
                    return type;
                }
            }

            return null;
        }

        public Stream<String> format(String colour) {
            return Arrays.stream(formats)
                .map(format -> String.format(format, colour));
        }
    }

}
