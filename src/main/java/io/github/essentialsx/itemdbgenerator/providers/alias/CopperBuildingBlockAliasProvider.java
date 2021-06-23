package io.github.essentialsx.itemdbgenerator.providers.alias;

import io.github.essentialsx.itemdbgenerator.providers.item.ItemProvider;
import org.bukkit.Material;

import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CopperBuildingBlockAliasProvider extends CompoundAliasProvider {

    @Override
    public Stream<String> get(ItemProvider.Item item) {
        CopperVariant copperVariant = CopperVariant.of(item.getMaterial());
        CopperBuildingBlock buildingBlock = CopperBuildingBlock.of(item.getMaterial());

        if (copperVariant == null || buildingBlock == null) return null;

        return getAliases(copperVariant, buildingBlock);
    }

    private enum CopperEffect implements CompoundModifier {
        // Copper conditions
        PURE("", "pure", "plain"),
        EXPOSED("exposed", "exp", "ex"),
        WEATHERED("weathered", "weather", "wth", "we"),
        OXIDIZED("oxidized", "oxidised", "oxi", "oxy"),

        // Copper "applied" effects
        CUT("cut", "c"),
        WAXED("waxed", "wax", "wa"),
        ;

        private final String[] names;

        CopperEffect(String... names) {
            this.names = names;
        }

        @Override
        public String[] getNames() {
            return names;
        }
    }

    // "plain", exposed, weathered and oxidized are mutually exclusive
    // all of the above can be cut, waxed, both or neither
    private enum CopperVariant implements MultipleCompoundModifier {
        WAXED_EXPOSED_CUT(null, "{WAXED}{EXPOSED}{CUT}", "{WAXED}{CUT}{EXPOSED}", "{CUT}{WAXED}{EXPOSED}", "{CUT}{EXPOSED}{WAXED}", "{EXPOSED}{CUT}{WAXED}", "{EXPOSED}{WAXED}{CUT}"),
        WAXED_WEATHERED_CUT(null, "{WAXED}{WEATHERED}{CUT}", "{WAXED}{CUT}{WEATHERED}", "{CUT}{WAXED}{WEATHERED}", "{CUT}{WEATHERED}{WAXED}", "{WEATHERED}{CUT}{WAXED}", "{WEATHERED}{WAXED}{CUT}"),
        WAXED_OXIDIZED_CUT(null, "{WAXED}{OXIDIZED}{CUT}", "{WAXED}{CUT}{OXIDIZED}", "{CUT}{WAXED}{OXIDIZED}", "{CUT}{OXIDIZED}{WAXED}", "{OXIDIZED}{CUT}{WAXED}", "{OXIDIZED}{WAXED}{CUT}"),
        WAXED_EXPOSED(null, "{WAXED}{EXPOSED}", "{EXPOSED}{WAXED}"),
        WAXED_WEATHERED(null, "{WAXED}{WEATHERED}", "{WEATHERED}{WAXED}"),
        WAXED_OXIDIZED(null, "{WAXED}{OXIDIZED}", "{OXIDIZED}{WAXED}"),
        WAXED_CUT(null, "{WAXED}{CUT}", "{CUT}{WAXED}"),
        EXPOSED_CUT(null, "{EXPOSED}{CUT}", "{CUT}{EXPOSED}"),
        WEATHERED_CUT(null, "{WEATHERED}{CUT}", "{CUT}{WEATHERED}"),
        OXIDIZED_CUT(null, "{OXIDIZED}{CUT}", "{CUT}{OXIDIZED}"),
        EXPOSED(null, "{EXPOSED}"),
        WEATHERED(null, "{WEATHERED}"),
        OXIDIZED(null, "{OXIDIZED}"),
        CUT(null, "{CUT}"),
        WAXED(null, "{WAXED}"),
        PLAIN("COPPER_BLOCK", "{PLAIN}"),
        ;

        private final Pattern regex;
        private final String[] names;

        CopperVariant(String regex, String... names) {
            this.regex = CompoundType.generatePattern(name(), regex);
            this.names = generateNames(name(), CopperEffect.values(), names);
        }

        public static CopperVariant of(Material material) {
            String matName = material.name();

            for (CopperVariant type : values()) {
                if (type.regex.matcher(matName).matches()) {
                    return type;
                }
            }

            return null;
        }

        @Override
        public String[] getNames() {
            return names;
        }
    }

    // for each CUT variant there's blocks, stairs and slabs
    private enum CopperBuildingBlock implements CompoundType {
        SLAB(null, "%scopperslab", "%scopslab", "%scoslab", "%scoppersl", "%scopsl", "%scosl", "%scopperstep", "%scopstep", "%scostep", "%scopperhalfblock", "%scophalfblock", "%scohalfblock"),
        STAIRS(null, "%scopperstairs", "%scopstairs", "%scostairs", "%scopperstair", "%scopstair", "%scostair"),
        BLOCK("^[A-Z]+_(SLAB|STAIRS)", true),
        ;

        private final Pattern regex;
        private final boolean invert;
        private final String[] formats;

        CopperBuildingBlock(String regex, String... formats) {
            this(regex, false, formats);
        }

        CopperBuildingBlock(String regex, boolean invert, String... formats) {
            this.regex = CompoundType.generatePattern(name(), regex);
            this.invert = invert;
            this.formats = CompoundType.generateFormats(name(), formats);
        }

        public static CopperBuildingBlock of(Material material) {
            String matName = material.name();

            for (CopperBuildingBlock type : values()) {
                if (type.invert != type.regex.matcher(matName).matches()) {
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
