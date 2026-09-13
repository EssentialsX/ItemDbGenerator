package io.github.essentialsx.itemdbgenerator.providers.alias;

import io.github.essentialsx.itemdbgenerator.providers.item.ItemProvider;
import org.bukkit.Material;

import java.util.stream.Stream;

public class ExplorerMapAliasProvider extends CompoundAliasProvider {
    private static final SingleCompoundType MAP_TYPE = new SingleCompoundType("^[A-Z_]+_MAP$", "%smap", "%sexplorermap", "%sexpmap", "%semap");

    @Override
    public Stream<String> get(ItemProvider.Item item) {
        ExplorerMapModifier modifier = ExplorerMapModifier.of(item.getMaterial());
        if (modifier == null || !MAP_TYPE.matches(item.getMaterial())) return null;

        return getAliases(modifier, MAP_TYPE);
    }

    private enum ExplorerMapModifier implements CompoundModifier {
        ABANDONED_CAMP("abandonedcamp", "acamp", "camp"),
        BURIED_ANCIENT_CITY("buriedancientcity", "ancientcity", "bancientcity", "acity", "bacity"),
        BURIED_MINESHAFT("buriedmineshaft", "mineshaft", "bmineshaft", "mshaft", "bmshaft"),
        BURIED_TREASURE("buriedtreasure", "treasure", "btreasure", "loot"),
        BURIED_TRIAL_CHAMBERS("buriedtrialchambers", "buriedtrialchamber", "trialchambers", "trialchamber", "btrialchambers", "btrialchamber", "trials", "btrials", "tchambers", "tchamber"),
        DESERT_PYRAMID("desertpyramid", "deserttemple", "dpyramid", "dtemple"),
        DESERT_VILLAGE("desertvillage", "dvillage"),
        JUNGLE_PYRAMID("junglepyramid", "jungletemple", "jpyramid", "jtemple"),
        OCEAN_MONUMENT("oceanmonument", "monument", "omonument", "oceantemple", "otemple"),
        PLAINS_VILLAGE("plainsvillage", "plainvillage", "pvillage"),
        SAVANNA_VILLAGE("savannavillage", "savvillage"),
        SNOWY_VILLAGE("snowyvillage", "snowvillage"),
        SWAMP_HUT("swamphut", "witchhut", "hut", "whut"),
        TAIGA_VILLAGE("taigavillage", "tvillage"),
        WARM_OCEAN_RUINS("warmoceanruins", "warmoceanruin", "oceanruins", "oceanruin", "woruins", "wruins", "ruins"),
        WOODLAND_MANSION("woodlandmansion", "mansion", "wmansion"),
        ;

        private final String[] names;

        ExplorerMapModifier(String... names) {
            this.names = names;
        }

        public static ExplorerMapModifier of(Material material) {
            String matName = material.name();

            for (ExplorerMapModifier map : values()) {
                if (matName.equals(map.name() + "_MAP")) {
                    return map;
                }
            }

            return null;
        }

        @Override
        public String[] getNames() {
            return names;
        }
    }
}
