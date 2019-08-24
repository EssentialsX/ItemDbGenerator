package io.github.essentialsx.itemdbgenerator.providers.alias;

import com.google.common.collect.ObjectArrays;
import io.github.essentialsx.itemdbgenerator.providers.item.ItemProvider;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class WoodAliasProvider extends CompoundAliasProvider {
    @Override
    public Stream<String> get(ItemProvider.Item item) {
        WoodType woodType = WoodType.of(item.getMaterial());
        WoodItemType itemType = WoodItemType.of(item.getMaterial());

        System.err.print("WOOD-" + woodType + "-" + itemType + " ");

        if (woodType == null || itemType == null) return null;

        return getAliases(woodType, itemType);
    }

    private Stream<String> getAliases(WoodType woodType, WoodItemType itemType) {
        return Arrays.stream(woodType.names)
            .flatMap(itemType::format);
    }

    /**
     * Represents available varieties of wood in the game.
     */
    @SuppressWarnings("unused")
    private enum WoodType {
        ACACIA("ac", "a"),
        BIRCH("b"),
        DARK_OAK("darkoak", "dark", "do"),
        JUNGLE("j"),
        OAK("o"),
        SPRUCE("sp", "s");

        private final String[] names;

        WoodType(String... names) {
            this.names = ObjectArrays.concat(name().toLowerCase(), names);
        }

        public static WoodType of(Material material) {
            String matName = material.name();
            for (WoodType type : values()) {
                if (matName.contains(type.name())) {
                    return type;
                }
            }

            return null;
        }
    }

    /**
     * Represents the types of items that can have multiple different wood types.
     */
    @SuppressWarnings("unused")
    private enum WoodItemType {
        BOAT(null, "boat%s", "%sboat"),
        BUTTON(null, "button%s", "%sbutton"),
        DOOR("[A-Z_]+_DOOR"),
        FENCE("[A-Z_]+_FENCE$"),
        FENCE_GATE(null, "%sgate", "%sfencegate", "gate%s"),
        LEAVES(null, "%sleaves", "%sleaf", "leaves%s", "leaf%s"),
        LOG("^[A-Z_]+_LOG", "log%s", "%slog"),
        PLANKS(null),
        PRESSURE_PLATE(null, "%spplate", "%spressureplate", "%splate", "plate%s"),
        SAPLING("^[A-Z_]+_SAPLING"),
        SLAB(null, "%sslab", "$shalfblock"),
        STAIRS(null, "%sstairs", "%sstair"),
        TRAPDOOR(null, "%strapdoor", "%stdoor"),
        WOOD("^[A-Z_]+_WOOD", "%swood", "wood%s"),
        POTTED_SAPLING("POTTED_[A-Z_]+_SAPLING", "%spot", "potted%s", "potted%ssapling"),
        STRIPPED_LOG("STRIPPED_[A-Z_]+_LOG", "stripped%slog", "log%sstripped", "str%slog"),
        STRIPPED_WOOD("STRIPPED_[A-Z_]+_WOOD", "stripped%swood", "wood%sstripped", "str%swood"),
        ;

        private final Pattern regex;
        private final String[] formats;

        WoodItemType(String regex, String... formats) {
            this.regex = getTypePattern(name(), regex);
            this.formats = getTypeFormats(name(), formats);
        }

        public static WoodItemType of(Material material) {
            String matName = material.name();

            for (WoodItemType type : values()) {
                if (type.regex.matcher(matName).matches()) {
                    return type;
                }
            }

            return null;
        }

        public Stream<String> format(String wood) {
            return Arrays.stream(formats)
                .map(format -> String.format(format, wood));
        }
    }
}
