package io.github.essentialsx.itemdbgenerator.providers.alias;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ObjectArrays;
import io.github.essentialsx.itemdbgenerator.providers.item.ItemProvider;
import org.bukkit.Material;

import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class MineableAliasProvider extends CompoundAliasProvider {

    private final Set<Material> skipped = ImmutableSet.of(
            Material.COPPER_BLOCK,
            Material.WAXED_COPPER_BLOCK
    );

    @Override
    public Stream<String> get(ItemProvider.Item item) {
        if (skipped.contains(item.getMaterial())) return null;

        Mineable mineable = Mineable.of(item.getMaterial());
        MineableItemType itemType = MineableItemType.of(item.getMaterial());

        if (mineable == null || itemType == null) return null;

        return getAliases(mineable, itemType);
    }

    /**
     * Represents available mineable materials in the game (and also leather, chainmail and wood for convenience)
     */
    @SuppressWarnings("unused")
    private enum Mineable implements CompoundModifier {
        NETHER_GOLD("ng", "nethgold", "hellgold"),
        NETHERITE("nether", "hell", "neth"),
        GOLD("g"),
        IRON("i", "steel", "s", "st"),
        COPPER("cop", "copp"),
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

        @Override
        public String[] getNames() {
            return names;
        }
    }

    /**
     * Represents the types of materials with ore variants.
     */
    @SuppressWarnings("unused")
    private enum MineableItemType implements CompoundType {
        // Caves and Cliffs
        RAW_ORE_BLOCK("RAW_[A-Z]+_BLOCK", "raw%soreblock", "%sorechunkblock", "r%soreblock", "raw%sorebl", "%sorechunkbl", "r%sorebl"),
        RAW_ORE("RAW_[A-Z]+_ORE", "raw%sore", "%sorechunk", "r%sore"),
        DEEPSLATE_ORE("DEEPSLATE_[A-Z]+_ORE", "deepslate%sore", "deep%sore", "slate%sore", "deepore%s", "dore%s"),
        // Nether Update
        NETHER_ORE("NETHER_[A-Z]+_ORE", "%sore", "%so", "ore%s", "o%s"),
        // Older ore
        ORE(null, "%sore", "%so", "ore%s", "o%s", "stone%sore"),
        BLOCK(null, "%sblock", "block%s"),
        INGOT(null, "%singot", "%sbar", "%si", "ingot%s", "bar%s", "i%s"),
        SCRAP(null, "%sscrap"),
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
        TRAPDOOR(null, "%strapdoor", "%sdoortrap", "%shatch", "%stdoor", "%sdoort", "%strapd", "%sdtrap");

        private final Pattern regex;
        private final String[] formats;

        MineableItemType(String regex, String... formats) {
            this.regex = CompoundType.generatePattern(name(), regex);
            this.formats = CompoundType.generateFormats(name(), formats);
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

        @Override
        public String[] getFormats() {
            return formats;
        }
    }
}
