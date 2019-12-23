package io.github.essentialsx.itemdbgenerator.providers.alias;

import com.google.common.collect.ObjectArrays;
import io.github.essentialsx.itemdbgenerator.providers.item.ItemProvider;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.bukkit.Material;

public class MineableAliasProvider extends CompoundAliasProvider {
    @Override
    public Stream<String> get(ItemProvider.Item item) {
        Mineable mineable = Mineable.of(item.getMaterial());
        MineableItemType itemType = MineableItemType.of(item.getMaterial());

//        System.err.print("ORETOOLS-" + mineable + "-" + itemType + " ");

        if (mineable == null || itemType == null) return null;

        return getAliases(mineable, itemType);
    }

    private Stream<String> getAliases(Mineable mineable, MineableItemType itemType) {
        return Arrays.stream(mineable.names)
            .flatMap(itemType::format);
    }

    /**
     * Represents available varieties of wood in the game.
     */
    @SuppressWarnings("unused")
    private enum Mineable {
        GOLD("g"),
        IRON("i", "steel", "s", "st"),
        COAL("c"),
        LAPIS("lapislazuli", "l"),
        DIAMOND("crystal", "d"),
        EMERALD("e"),
        REDSTONE("reds", "red", "rstone", "rs", "r"),
        NETHER_QUARTZ("netherquartz", "hellquartz", "deathquartz", "nquartz", "hquartz", "dquartz", "quartz", "netherq", "hellq", "deathq", "nq", "hq", "dq", "q"),
        // While not ores, these have armor and tools
        LEATHER("l"),
        CHAINMAIL("chainm", "cmail", "chain", "cm"),
        WOODEN("wood", "w"),
        STONE("cobblestone", "cstone", "cs", "s");

        private final String[] names;

        Mineable(String... names) {
            this.names = ObjectArrays.concat(name().toLowerCase(), names);
        }

        public static Mineable of(Material material) {
            String matName = material.name();

            for (Mineable mineable : values()) {
                if (matName.contains(mineable.name())) {
                    return mineable;
                }
            }

            return null;
        }
    }

    /**
     * Represents the types of materials with coloured variants.
     */
    @SuppressWarnings("unused")
    private enum MineableItemType {
        // Blocks
        ORE(null, "%sore", "%so", "!ore%s", "!o%s"),
        BLOCK(null, "%sblock", "block%s"),
        INGOT(null, "%singot", "%sbar", "%si", "!ingot%s", "!bar%s", "!i%s"),
        // Tools
        SWORD(null, "%ssword"),
        SHOVEL(null, "%sshovel", "%sspade"),
        PICKAXE(null, "%spickaxe", "%spick"),
        AXE("[A-Z_]+_(?<!PICK)AXE", "%saxe"),
        HOE(null, "%shoe"),
        // Armour
        HELMET(null, "%shelmet", "%shelm", "%shat", "%scoif"),
        CHESTPLATE(null, "%schestplate", "%splatebody", "%splate", "%sshirt", "%stunic"),
        LEGGINGS(null, "%sleggings", "%slegs", "%spants"),
        BOOTS(null, "%sboots", "%sshoes"),
        HORSE_ARMOR(null, "%shorsearmor", "%sharmor", "%sarmor"),
        // Doors
        DOOR("[A-Z_]+_(?<!TRAP)DOOR", "%sdoor", "door%s"),
        TRAPDOOR(null, "%strapdoor", "%sdoortrap", "%shatch", "%stdoor", "%sdoort", "%strapd", "%sdtrap")
        ;

        private final Pattern regex;
        private final String[] formats;

        MineableItemType(String regex, String... formats) {
            this.regex = getTypePattern(name(), regex);
            this.formats = getTypeFormats(name(), formats);
        }

        public static MineableItemType of(Material material) {
            String matName = material.name();

            for (MineableItemType type : values()) {
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
