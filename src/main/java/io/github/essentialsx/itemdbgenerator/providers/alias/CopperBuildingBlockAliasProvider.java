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
        CHISELED("chiseled", "chis", "chi"),
        BULB("bulb", "blb", "bl"),
        DOOR("door", "dr"),
        TRAPDOOR("trapdoor", "trapd", "td"),
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
        CUT("^CUT_COPPER(?:_SLAB|_STAIRS)?", "{CUT}"),
        WAXED(null, "{WAXED}"),
        PLAIN("COPPER_BLOCK", "{PURE}"),
        CHISELED("^CHISELED_COPPER(?:_SLAB|_STAIRS)?", "{CHISELED}"),
        WAXED_EXPOSED_CHISELED(null, "{WAXED}{EXPOSED}{CHISELED}", "{WAXED}{CHISELED}{EXPOSED}", "{CHISELED}{WAXED}{EXPOSED}", "{CHISELED}{EXPOSED}{WAXED}", "{EXPOSED}{CHISELED}{WAXED}", "{EXPOSED}{WAXED}{CHISELED}"),
        WAXED_WEATHERED_CHISELED(null, "{WAXED}{WEATHERED}{CHISELED}", "{WAXED}{CHISELED}{WEATHERED}", "{CHISELED}{WAXED}{WEATHERED}", "{CHISELED}{WEATHERED}{WAXED}", "{WEATHERED}{CHISELED}{WAXED}", "{WEATHERED}{WAXED}{CHISELED}"),
        WAXED_OXIDIZED_CHISELED(null, "{WAXED}{OXIDIZED}{CHISELED}", "{WAXED}{CHISELED}{OXIDIZED}", "{CHISELED}{WAXED}{OXIDIZED}", "{CHISELED}{OXIDIZED}{WAXED}", "{OXIDIZED}{CHISELED}{WAXED}", "{OXIDIZED}{WAXED}{CHISELED}"),
        WAXED_CHISELED(null, "{WAXED}{CHISELED}", "{CHISELED}{WAXED}"),
        EXPOSED_CHISELED(null, "{EXPOSED}{CHISELED}", "{CHISELED}{EXPOSED}"),
        WEATHERED_CHISELED(null, "{WEATHERED}{CHISELED}", "{CHISELED}{WEATHERED}"),
        OXIDIZED_CHISELED(null, "{OXIDIZED}{CHISELED}", "{CHISELED}{OXIDIZED}"),
        WAXED_EXPOSED_BULB(null, "{WAXED}{EXPOSED}{BULB}", "{WAXED}{BULB}{EXPOSED}", "{BULB}{WAXED}{EXPOSED}", "{BULB}{EXPOSED}{WAXED}", "{EXPOSED}{BULB}{WAXED}", "{EXPOSED}{WAXED}{BULB}"),
        WAXED_WEATHERED_BULB(null, "{WAXED}{WEATHERED}{BULB}", "{WAXED}{BULB}{WEATHERED}", "{BULB}{WAXED}{WEATHERED}", "{BULB}{WEATHERED}{WAXED}", "{WEATHERED}{BULB}{WAXED}", "{WEATHERED}{WAXED}{BULB}"),
        WAXED_OXIDIZED_BULB(null, "{WAXED}{OXIDIZED}{BULB}", "{WAXED}{BULB}{OXIDIZED}", "{BULB}{WAXED}{OXIDIZED}", "{BULB}{OXIDIZED}{WAXED}", "{OXIDIZED}{BULB}{WAXED}", "{OXIDIZED}{WAXED}{BULB}"),
        WAXED_BULB(null, "{WAXED}{BULB}", "{BULB}{WAXED}"),
        EXPOSED_BULB(null, "{EXPOSED}{BULB}", "{BULB}{EXPOSED}"),
        WEATHERED_BULB(null, "{WEATHERED}{BULB}", "{BULB}{WEATHERED}"),
        OXIDIZED_BULB(null, "{OXIDIZED}{BULB}", "{BULB}{OXIDIZED}"),
        BULB("^COPPER_BULB(?:_SLAB|_STAIRS)?", "{BULB}"),
        WAXED_EXPOSED_DOOR(null, "{WAXED}{EXPOSED}{DOOR}", "{WAXED}{DOOR}{EXPOSED}", "{DOOR}{WAXED}{EXPOSED}", "{DOOR}{EXPOSED}{WAXED}", "{EXPOSED}{DOOR}{WAXED}", "{EXPOSED}{WAXED}{DOOR}"),
        WAXED_WEATHERED_DOOR(null, "{WAXED}{WEATHERED}{DOOR}", "{WAXED}{DOOR}{WEATHERED}", "{DOOR}{WAXED}{WEATHERED}", "{DOOR}{WEATHERED}{WAXED}", "{WEATHERED}{DOOR}{WAXED}", "{WEATHERED}{WAXED}{DOOR}"),
        WAXED_OXIDIZED_DOOR(null, "{WAXED}{OXIDIZED}{DOOR}", "{WAXED}{DOOR}{OXIDIZED}", "{DOOR}{WAXED}{OXIDIZED}", "{DOOR}{OXIDIZED}{WAXED}", "{OXIDIZED}{DOOR}{WAXED}", "{OXIDIZED}{WAXED}{DOOR}"),
        WAXED_DOOR(null, "{WAXED}{DOOR}", "{DOOR}{WAXED}"),
        EXPOSED_DOOR(null, "{EXPOSED}{DOOR}", "{DOOR}{EXPOSED}"),
        WEATHERED_DOOR(null, "{WEATHERED}{DOOR}", "{DOOR}{WEATHERED}"),
        OXIDIZED_DOOR(null, "{OXIDIZED}{DOOR}", "{DOOR}{OXIDIZED}"),
        DOOR("^COPPER_DOOR(?:_SLAB|_STAIRS)?", "{DOOR}"),
        WAXED_EXPOSED_TRAPDOOR(null, "{WAXED}{EXPOSED}{TRAPDOOR}", "{WAXED}{TRAPDOOR}{EXPOSED}", "{TRAPDOOR}{WAXED}{EXPOSED}", "{TRAPDOOR}{EXPOSED}{WAXED}", "{EXPOSED}{TRAPDOOR}{WAXED}", "{EXPOSED}{WAXED}{TRAPDOOR}"),
        WAXED_WEATHERED_TRAPDOOR(null, "{WAXED}{WEATHERED}{TRAPDOOR}", "{WAXED}{TRAPDOOR}{WEATHERED}", "{TRAPDOOR}{WAXED}{WEATHERED}", "{TRAPDOOR}{WEATHERED}{WAXED}", "{WEATHERED}{TRAPDOOR}{WAXED}", "{WEATHERED}{WAXED}{TRAPDOOR}"),
        WAXED_OXIDIZED_TRAPDOOR(null, "{WAXED}{OXIDIZED}{TRAPDOOR}", "{WAXED}{TRAPDOOR}{OXIDIZED}", "{TRAPDOOR}{WAXED}{OXIDIZED}", "{TRAPDOOR}{OXIDIZED}{WAXED}", "{OXIDIZED}{TRAPDOOR}{WAXED}", "{OXIDIZED}{WAXED}{TRAPDOOR}"),
        WAXED_TRAPDOOR(null, "{WAXED}{TRAPDOOR}", "{TRAPDOOR}{WAXED}"),
        EXPOSED_TRAPDOOR(null, "{EXPOSED}{TRAPDOOR}", "{TRAPDOOR}{EXPOSED}"),
        WEATHERED_TRAPDOOR(null, "{WEATHERED}{TRAPDOOR}", "{TRAPDOOR}{WEATHERED}"),
        OXIDIZED_TRAPDOOR(null, "{OXIDIZED}{TRAPDOOR}", "{TRAPDOOR}{OXIDIZED}"),
        TRAPDOOR("^COPPER_TRAPDOOR(?:_SLAB|_STAIRS)?", "{TRAPDOOR}"),
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
        BLOCK("^[A-Z]+_(SLAB|STAIRS)", true, "%scopperblock", "%scopblock", "%scoblock"),
        BULB(null, "%scopperbulb", "%scopbulb", "%scobulb"),
        DOOR(null, "%scopperdoor", "%scopdoor", "%scodoor"),
        TRAPDOOR(null, "%scoppertrapdoor", "%scoptrapdoor", "%scotrapdoor", "%scotrapd", "%scophatch"),
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
