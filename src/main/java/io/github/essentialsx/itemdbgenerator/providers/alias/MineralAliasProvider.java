package io.github.essentialsx.itemdbgenerator.providers.alias;

import io.github.essentialsx.itemdbgenerator.providers.item.ItemProvider;
import org.bukkit.Material;

import java.util.regex.Pattern;
import java.util.stream.Stream;

public class MineralAliasProvider extends CompoundAliasProvider {

    @Override
    public Stream<String> get(ItemProvider.Item item) {
        Mineral mineral = Mineral.of(item.getMaterial());
        MineralItemType itemType = MineralItemType.of(item.getMaterial());

        if (mineral == null || itemType == null) return null;

        return getAliases(mineral, itemType);
    }

    private enum MineralModifier implements CompoundModifier {
        CHISELED("chiseled", "circle", "ci"),
        COBBLED("cobbled", "cobble", "cb", "cob"),
        CRACKED("cracked", "crack", "cr"),
        CUT("cut"),
        INFESTED("infested", "infest", "silverfish", "sfish", "fish", "infested", "monsteregg", "megg", "trap", "sf", "me"),
        MOSSY("mossy", "moss", "m"),
        SMOOTH("smooth", "sm"),
        POLISHED("polished", "p"),
        PACKED("packed", "pack")
        ;

        private final String[] names;

        MineralModifier(String... names) {
            this.names = names;
        }

        @Override
        public String[] getNames() {
            return names;
        }
    }

    private enum Mineral implements MultipleCompoundModifier {
        INFESTED_CHISELED_STONE(null, "{INFESTED}{CHISELED}st", "{INFESTED}{CHISELED}stone", "{CHISELED}{INFESTED}st", "{CHISELED}{INFESTED}stone"),
        INFESTED_CRACKED_STONE(null, "{INFESTED}{CRACKED}st", "{INFESTED}{CRACKED}stone", "{CRACKED}{INFESTED}st", "{CRACKED}{INFESTED}stone"),
        INFESTED_MOSSY_STONE(null, "{INFESTED}{MOSSY}st", "{INFESTED}{MOSSY}stone", "{MOSSY}{INFESTED}st", "{MOSSY}{INFESTED}stone"),
        INFESTED_COBBLESTONE(null, "{INFESTED}cobble", "{INFESTED}cstone", "{INFESTED}cs", "{INFESTED}cst", "{INFESTED}cobblestone"),
        INFESTED_DEEPSLATE(null, "{INFESTED}deepslate", "{INFESTED}dslate", "{INFESTED}slate"),
        INFESTED_STONE_BRICKS(null, "{INFESTED}stbrick", "{INFESTED}stbr", "{INFESTED}stonebr", "{INFESTED}stonebrick", "{INFESTED}stonebricks"),
        INFESTED_STONE(null, "{INFESTED}st", "{INFESTED}stone"),
        CHISELED_NETHER_BRICK(null, "{CHISELED}nbr", "{CHISELED}nbrick", "{CHISELED}nethbr", "{CHISELED}nethbrick", "{CHISELED}netherbr", "{CHISELED}netherbrick"),
        CHISELED_POLISHED_BLACKSTONE(null, "{CHISELED}{POLISHED}blstone", "{CHISELED}{POLISHED}blst", "{CHISELED}{POLISHED}blackstone", "{POLISHED}{CHISELED}blstone", "{POLISHED}{CHISELED}blst", "{POLISHED}{CHISELED}blackstone"),
        CHISELED_QUARTZ(null, "{CHISELED}quartz", "{CHISELED}quar", "{CHISELED}q"),
        CHISELED_RED_SANDSTONE(null, "{CHISELED}redsandst", "{CHISELED}redsndst", "{CHISELED}redsandstone"),
        CHISELED_SANDSTONE(null, "{CHISELED}sandst", "{CHISELED}sndst", "{CHISELED}sandstone"),
        CHISELED_DEEPSLATE(null, "{CHISELED}deepslate", "{CHISELED}dslate", "{CHISELED}slate"),
        CHISELED_STONE(null, "{CHISELED}st", "{CHISELED}stone"),
        CRACKED_NETHER_BRICK(null, "{CRACKED}nbr", "{CRACKED}nbrick", "{CRACKED}nethbr", "{CRACKED}nethbrick", "{CRACKED}netherbr", "{CRACKED}netherbrick"),
        CRACKED_POLISHED_BLACKSTONE(null, "{CRACKED}{POLISHED}blstone", "{CRACKED}{POLISHED}blst", "{CRACKED}{POLISHED}blackstone", "{POLISHED}{CRACKED}blstone", "{POLISHED}{CRACKED}blst", "{POLISHED}{CRACKED}blackstone"),
        CRACKED_DEEPSLATE_BRICKS(null, "{CRACKED}deepslatebricks", "{CRACKED}dslatebricks", "{CRACKED}slatebricks", "{CRACKED}deepslatebr", "{CRACKED}dslatebr", "{CRACKED}slatebr"),
        CRACKED_DEEPSLATE(null, "{CRACKED}deepslate", "{CRACKED}dslate", "{CRACKED}slate"),
        CRACKED_STONE(null, "{CRACKED}st", "{CRACKED}stone"),
        COBBLED_DEEPSLATE(null, "{COBBLED}deepslate", "{COBBLED}dslate", "{COBBLED}slate"),
        CUT_RED_SANDSTONE(null, "{CUT}redsandst", "{CUT}redsndst", "{CUT}redsandstone"),
        CUT_SANDSTONE(null, "{CUT}sandst", "{CUT}sndst", "{CUT}sandstone"),
        MOSSY_COBBLESTONE(null, "{MOSSY}cobble", "{MOSSY}cstone", "{MOSSY}cs", "{MOSSY}cst", "{MOSSY}cobblestone"),
        MOSSY_STONE(null, "{MOSSY}st", "{MOSSY}stone"),
        SMOOTH_BASALT(null, "{SMOOTH}basalt", "{SMOOTH}bast", "{SMOOTH}basaltst", "{SMOOTH}bas"),
        SMOOTH_QUARTZ(null, "{SMOOTH}quartz", "{SMOOTH}quar", "{SMOOTH}q"),
        SMOOTH_RED_SANDSTONE(null, "{SMOOTH}redsandstone", "{SMOOTH}redsandst", "{SMOOTH}redsndst"),
        SMOOTH_SANDSTONE(null, "{SMOOTH}sandstone", "{SMOOTH}sandst", "{SMOOTH}sndst"),
        SMOOTH_STONE(null, "{SMOOTH}st", "{SMOOTH}stone"),
        POLISHED_ANDESITE(null, "{POLISHED}andstone", "{POLISHED}astone", "{POLISHED}and", "{POLISHED}andesite"),
        POLISHED_BASALT(null, "{POLISHED}bast", "{POLISHED}basaltst", "{POLISHED}basalt"),
        POLISHED_BLACKSTONE_BRICK(null, "{POLISHED}blstonebrick", "{POLISHED}blstbrick", "{POLISHED}blstonebr", "{POLISHED}blstbr", "{POLISHED}blackstonebrick"),
        POLISHED_BLACKSTONE(null, "{POLISHED}blstone", "{POLISHED}blst", "{POLISHED}blackstone"),
        POLISHED_DIORITE(null, "{POLISHED}di", "{POLISHED}dstone", "{POLISHED}diorite"),
        POLISHED_GRANITE(null, "{POLISHED}gr", "{POLISHED}gstone", "{POLISHED}granite"),
        POLISHED_DEEPSLATE(null, "{POLISHED}deepslate", "{POLISHED}dslate", "{POLISHED}slate"),
        PACKED_MUD(null, "{PACKED}mud"),
        DRIPSTONE(null, "dripstone", "drip"),
        COBBLESTONE(null, "cobble", "cstone", "cs", "cst"),
        ANDESITE(null, "astone", "andstone", "and"),
        BASALT(null, "bast", "basaltst", "bas"),
        GILDED_BLACKSTONE(null, "gildblstone", "gildblackstone", "gildblst"),
        BLACKSTONE(null, "blstone", "blst"),
        RED_NETHER_BRICK(null, "rnbr", "rnbrick", "rednbr", "rednbrick", "rnetherbr", "rnetherbrick", "rednetherbr", "rednetherbrick"),
        NETHER_BRICK(null, "nbr", "nbrick", "nethbr", "nethbrick", "netherbr", "netherbrick"),
        DIORITE(null, "di", "dstone"),
        END_STONE_BRICK(null, "endstbr", "whstbr"),
        END_STONE(null, "endst", "whst"),
        GRANITE(null, "gr", "gstone"),
        QUARTZ_BRICK(null, "quartzbr", "quarbr", "qbr", "quartzbrick", "quarbrick", "qbrick", "quartzbricks", "quarbricks", "qbricks"),
        QUARTZ(null, "quar", "q", "netherquartz", "nq"),
        RED_SANDSTONE(null, "redsandst", "redsndst"),
        SANDSTONE(null, "sandst", "sndst"),
        DEEPSLATE_TILE(null, "deepslatetile", "dslatetile", "slatetile", "deepslatetiles", "dslatetiles", "slatetiles"),
        DEEPSLATE_BRICK(null, "deepslatebricks", "dslatebricks", "slatebricks", "deepslatebr", "dslatebr", "slatebr"),
        DEEPSLATE(null, "deepslate", "dslate", "slate"),
        STONE_BRICK(null, "stbrick", "stbr", "stonebr"),
        STONE("^(?!LODE|GRIND|END|DRIP)", "st"),
        MUD_BRICK(null, "mudbrick", "mudbr"),
        MUD("^MUD(?!DY)", "mud")
        ;

        private final Pattern regex;
        private final String[] names;

        Mineral(String regex, String... names) {
            this.regex = CompoundType.generatePattern(name(), regex);
            this.names = generateNames(name(), MineralModifier.values(), names);
        }

        public static Mineral of(Material material) {
            String matName = material.name();

            for (Mineral type : values()) {
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

    private enum MineralItemType implements CompoundType {
        BUTTON(null, "button%s", "%sbutton"),
        BRICKS(null, "%s"),
        FENCE(null, "%sfence", "fence%s"),
        PILLAR(null, "%spillar", "%spill"),
        PRESSURE_PLATE(null, "%spplate", "%spressureplate", "%splate", "plate%s", "%spressplate"),
        SLAB("^[A-Z_]+_SLAB", "%sstep", "%shalfblock"),
        STAIRS(null, "%sstairs", "%sstair"),
        TILES(null, "%stiles", "%stile", "tile%s", "tiles%s"),
        WALL(null, "%swall", "wall%s"),
        ITEM("^(NETHER_BRICK|QUARTZ)$", "%s"),
        POINTED("^POINTED_DRIPSTONE", "pointed%s", "point%s", "p%s", "hanging%s", "spiky%s"),
        BLOCK("^[A-Z_]+_(AXE|SHOVEL|SWORD|HOE|ORE|BUTTON|BRICKS|PRESSURE_PLATE|SLAB|STAIRS|WALL)", true, "%s", "%sb", "%sbl", "%sblock"),
        ;

        private final Pattern regex;
        private final boolean invert;
        private final String[] formats;

        MineralItemType(String regex, String... formats) {
            this(regex, false, formats);
        }

        MineralItemType(String regex, boolean invert, String... formats) {
            this.regex = CompoundType.generatePattern(name(), regex);
            this.invert = invert;
            this.formats = CompoundType.generateFormats(name(), formats);
        }

        public static MineralItemType of(Material material) {
            String matName = material.name();

            for (MineralItemType type : values()) {
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
