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
        CHISELED("chiseled", "circle", "ci"),
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
        WAXED_EXPOSED_CHISELED(null, "{WAXED}{EXPOSED}{CHISELED}", "{WAXED}{CHISELED}{EXPOSED}", "{CHISELED}{WAXED}{EXPOSED}", "{CHISELED}{EXPOSED}{WAXED}", "{EXPOSED}{CHISELED}{WAXED}", "{EXPOSED}{WAXED}{CHISELED}"),
        WAXED_WEATHERED_CHISELED(null, "{WAXED}{WEATHERED}{CHISELED}", "{WAXED}{CHISELED}{WEATHERED}", "{CHISELED}{WAXED}{WEATHERED}", "{CHISELED}{WEATHERED}{WAXED}", "{WEATHERED}{CHISELED}{WAXED}", "{WEATHERED}{WAXED}{CHISELED}"),
        WAXED_OXIDIZED_CHISELED(null, "{WAXED}{OXIDIZED}{CHISELED}", "{WAXED}{CHISELED}{OXIDIZED}", "{CHISELED}{WAXED}{OXIDIZED}", "{CHISELED}{OXIDIZED}{WAXED}", "{OXIDIZED}{CHISELED}{WAXED}", "{OXIDIZED}{WAXED}{CHISELED}"),
        WAXED_EXPOSED(null, "{WAXED}{EXPOSED}", "{EXPOSED}{WAXED}"),
        WAXED_WEATHERED(null, "{WAXED}{WEATHERED}", "{WEATHERED}{WAXED}"),
        WAXED_OXIDIZED(null, "{WAXED}{OXIDIZED}", "{OXIDIZED}{WAXED}"),
        WAXED_CUT(null, "{WAXED}{CUT}", "{CUT}{WAXED}"),
        EXPOSED_CUT(null, "{EXPOSED}{CUT}", "{CUT}{EXPOSED}"),
        WEATHERED_CUT(null, "{WEATHERED}{CUT}", "{CUT}{WEATHERED}"),
        OXIDIZED_CUT(null, "{OXIDIZED}{CUT}", "{CUT}{OXIDIZED}"),
        WAXED_CHISELED(null, "{WAXED}{CHISELED}", "{CHISELED}{WAXED}"),
        EXPOSED_CHISELED(null, "{EXPOSED}{CHISELED}", "{CHISELED}{EXPOSED}"),
        WEATHERED_CHISELED(null, "{WEATHERED}{CHISELED}", "{CHISELED}{WEATHERED}"),
        OXIDIZED_CHISELED(null, "{OXIDIZED}{CHISELED}", "{CHISELED}{OXIDIZED}"),
        EXPOSED(null, "{EXPOSED}"),
        WEATHERED(null, "{WEATHERED}"),
        OXIDIZED(null, "{OXIDIZED}"),
        CHISELED("^CHISELED_COPPER", "{CHISELED}"),
        CUT("^CUT_COPPER(?:_SLAB|_STAIRS)?", "{CUT}"),
        WAXED(null, "{WAXED}"),
        PLAIN("COPPER_BLOCK|CHISELED_COPPER", "{PURE}"),
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
        DOOR("[A-Z_]+_(?<!TRAP)DOOR", "%sdoor", "door%s"),
        TRAPDOOR(null, "%strapdoor", "%sdoortrap", "%shatch", "%stdoor", "%sdoort", "%strapd", "%sdtrap"),
        BULB(null, "%sbulb"),
        GRATE(null, "%sgrate"),
        BLOCK("^[A-Z]+_(SLAB|STAIRS)", true, "%scopperblock", "%scopblock", "%scoblock"),
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
