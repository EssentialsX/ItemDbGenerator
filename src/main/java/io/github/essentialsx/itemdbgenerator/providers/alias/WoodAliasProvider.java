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
        BIRCH("b", "light", "l", "white", "w"),
        DARK_OAK("dark", "do"),
        JUNGLE("j", "forest", "f"),
        OAK("o"),
        SPRUCE("pine", "p", "dark", "d", "s");

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
        BOAT(null, "boat%s", "%sboat", "%sraft"),
        BUTTON(null, "button%s", "%sbutton"),
        DOOR("[A-Z_]+_DOOR"),
        FENCE("[A-Z_]+_FENCE$"),
        FENCE_GATE(null, "%sgate", "%sfencegate", "gate%s"),
        LEAVES(null, "%sleaves", "%sleaf", "leaves%s", "leaf%s", "%streeleaves", "%slogleaves", "%strunkleaves", "%swoodleaves", "%streeleaf", "%slogleaf", "%strunkleaf", "%swoodleaf", "%sleaf", "%streeleave", "%slogleave", "%strunkleave", "%swoodleave", "%sleave"),
        LOG("^[A-Z_]+_LOG", "log%s", "%slog", "%strunk", "%stree"),
        PLANKS(null, "%swoodenplank", "%swoodplank", "%swplank", "%splankwooden", "%splankwood", "%splankw", "%splank"),
        PRESSURE_PLATE(null, "%spplate", "%spressureplate", "%splate", "plate%s", "%spressplate"),
        SAPLING("^[A-Z_]+_SAPLING", "%ssapling", "%streesapling", "%slogsapling", "%strunksapling", "%swoodsapling"),
        SLAB(null, "%swoodenstep", "%swoodstep", "%swstep", "%sstep", "%swoodenslab", "%swoodslab", "%swslab", "%swoodenhalfblock", "%swoodhalfblock", "%swhalfblock", "%shalfblock"),
        STAIRS(null, "%swoodenstairs", "%swoodstairs", "%swstairs", "%swoodenstair", "%swoodstair", "%swstair", "%sstair"),
        TRAPDOOR(null, "%strapdoor", "%sdoortrap", "%shatch", "%stdoor", "%sdoort", "%strapd", "%sdtrap"),
        WOOD("^[A-Z_]+_WOOD", "%swood", "%slogall", "%strunkall", "%streeall", "wood%s"),
        POTTED_SAPLING("POTTED_[A-Z_]+_SAPLING", "%spot", "potted%s", "potted%ssapling"),
        STRIPPED_LOG("STRIPPED_[A-Z_]+_LOG", "stripped%slog", "log%sstripped", "str%slog", "%sstrippedlog", "%sbarelog", "stripped%stree", "bare%stree", "stripped%strunk", "bare%strunk"),
        STRIPPED_WOOD("STRIPPED_[A-Z_]+_WOOD", "stripped%swood", "wood%sstripped", "str%swood", "%sstrippedwood", "%sbarewood", "stripped%slogall", "bare%slogall", "stripped%strunkall", "bare%strunkall", "stripped%streeall", "bare%streeall"),
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
