package io.github.essentialsx.itemdbgenerator.providers.alias;

import io.github.essentialsx.itemdbgenerator.providers.item.ItemProvider;
import io.github.essentialsx.itemdbgenerator.providers.item.SpawnerProvider;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.stream.Stream;

public class FixedAliasProvider implements AliasProvider {
    private static final EnumMap<Material, String[]> FIXED_ALIASES = new EnumMap<>(Material.class);
    private static final EnumMap<EntityType, String[]> FIXED_SPAWNER_ALIASES = new EnumMap<>(EntityType.class);

    private static void add(Material material, String... aliases) {
        FIXED_ALIASES.put(material, aliases);
    }

    private static void add(EntityType entity, String... aliases) {
        FIXED_SPAWNER_ALIASES.put(entity, aliases);
    }

    static {
        add(Material.BEDROCK, "oprock", "opblock", "adminblock", "adminrock", "adminium");
        add(Material.OBSIDIAN, "obsi", "obby");
        // == Interactive ==
        add(Material.CHEST, "container", "drawer");
        add(Material.CRAFTING_TABLE, "workbench", "craftingbench", "crafterbench", "craftbench", "worktable", "craftertable", "crafttable", "wbench", "cbench");
        add(Material.ENCHANTING_TABLE, "enchantmenttable", "enchanttable", "etable", "magicaltable", "magictable", "mtable", "enchantmentdesk", "enchantingdesk", "enchantdesk", "edesk", "magicaldesk", "magicdesk", "mdesk", "booktable", "bookdesk", "btable", "bdesk");
        add(Material.ENDER_CHEST, "endchest", "echest", "chestender", "chestend", "cheste", "endercontainer", "endcontainer", "econtainer");
        add(Material.BEACON, "beaconblock");
        add(Material.CHIPPED_ANVIL, "slightlydamagedanvil", "slightdamageanvil");
        add(Material.DAMAGED_ANVIL, "verydamagedanvil");
        // == Dirt ==
        add(Material.GRASS_BLOCK, "greendirt", "greenearth", "greenland");
        add(Material.DIRT_PATH, "path", "gpath", "grasspath", "grass_path", "minecraft:grass_path", "dpath");
        add(Material.DIRT, "earth", "land");
        add(Material.COARSE_DIRT, "cdirt", "grasslessdirt", "grasslessearth", "grasslessland", "coarseland", "coarseearth");
        add(Material.ROOTED_DIRT, "rsoil", "rland", "rearth", "rootedsoil", "rootedland", "rootedearth");
        add(Material.MYCELIUM, "mycel", "swampgrass", "sgrass", "mushroomgrass", "mushgrass");
        add(Material.SUSPICIOUS_GRAVEL, "susgravel", "amongusgravel");
        add(Material.SUSPICIOUS_SAND, "sussand", "amongussand");
        // == Redstone ==
        add(Material.REDSTONE_TORCH, "rstonetorch", "redstorch", "redtorch", "rstorch");
        add(Material.DISPENSER, "dispense");
        add(Material.NOTE_BLOCK, "musicblock", "nblock", "mblock");
        add(Material.JUKEBOX, "jbox");
        add(Material.TNT, "tntblock", "blocktnt", "bombblock", "blockbomb", "dynamiteblock", "blockdynamite", "bomb", "dynamite");
        add(Material.TRIPWIRE_HOOK, "trip", "tripwirelever", "triphook");
        add(Material.TRAPPED_CHEST, "trapchest", "chesttrapped", "chesttrap");
        add(Material.DAYLIGHT_DETECTOR, "daylightsensor", "daylightsense", "lightsensor", "lightsense", "daysensor", "daysense", "timesensor", "timesense");
        add(Material.HOPPER, "chestpuller", "chestpull", "cheststorer", "cheststore", "itempuller", "itempull", "itemstorer", "itemstore");
        add(Material.STONE_PRESSURE_PLATE, "smoothstonepressueplate", "smoothstonepressplate", "smoothstonepplate", "smoothstoneplate", "sstonepressureplate", "sstonepressplate", "sstonepplate", "sstoneplate");
        add(Material.COMMAND_BLOCK, "blockcommand", "cmdblock", "blockcmd", "macroblock", "blockmacro");
        add(Material.CHAIN_COMMAND_BLOCK, "chaincmdblock", "chcmdblock", "chainmacroblock", "chblockcmd");
        add(Material.REPEATING_COMMAND_BLOCK, "repcmdblock", "loopcmdblock", "loopmacroblock", "loopblockcmd");
        add(Material.DAYLIGHT_DETECTOR, "lightdetector", "photoresistor", "daydetector", "lightdetect", "solarpanel", "daydetect");
        add(Material.SCULK_CATALYST, "sccatalyst", "sculkcat", "sccat");
        add(Material.SCULK_SENSOR, "scsensor");
        add(Material.SCULK_SHRIEKER, "scshrieker", "sculkshriek", "scshriek");
        add(Material.SCULK_VEIN, "scvein");
        add(Material.CALIBRATED_SCULK_SENSOR, "cscsensor");
        // == Decorative ==
        add(Material.RED_SAND, "rsand");
        add(Material.GLASS, "blockglass", "glassblock");
        add(Material.GLASS_PANE, "glassp", "paneglass", "pglass", "flatglass", "fglass", "skinnyglass", "glassflat", "glassf", "glassskinny", "glasss");
        add(Material.TINTED_GLASS, "glasstinted", "tintglass", "glasstint", "tglass", "glasst", "lightblockingglass");
        add(Material.BOOKSHELF, "bshelf", "bookcase", "casebook", "shelfbook", "bookblock", "blockbook");
        add(Material.CHISELED_BOOKSHELF, "cbshelf", "cbookcase", "cshelfbook", "cbookblock", "cblockbook", "chiseledshelf", "chiseledb");
        add(Material.TORCH, "burningstick", "burnstick");
        add(Material.GLOWSTONE, "glowingstoneblock", "lightstoneblock", "glowstoneblock", "blockglowingstone", "blocklightstone", "blockglowstone", "glowingstone", "lightstone", "glowingblock", "lightblock", "glowblock", "lstone");
        add(Material.LILY_PAD, "waterlily", "lily", "swamppad", "lpad", "wlily");
        add(Material.ANCIENT_DEBRIS, "debris");
        add(Material.CRYING_OBSIDIAN, "cryobsidian", "sadrock");
        add(Material.RESPAWN_ANCHOR, "respawnpoint", "spawnanchor", "respawnanc", "spawnanc", "netherbed");
        add(Material.LODESTONE, "lode");
        add(Material.SHROOMLIGHT, "shroomlamp", "netherlamp", "shlight");
        add(Material.LIGHT, "lightblock");
        add(Material.ITEM_FRAME, "iframe", "frame");
        add(Material.GLOW_BERRIES, "glowfruit");
        add(Material.GLOW_INK_SAC, "glowink", "glowsac");
        add(Material.GLOW_ITEM_FRAME, "glowiframe", "glowframe", "glframe");
        add(Material.GLOW_LICHEN, "glowmoss");
        add(Material.SPORE_BLOSSOM, "sporeflower", "spblossom", "spflower", "lushflower");
        add(Material.BIG_DRIPLEAF, "bigdrip", "talldripleaf", "talldrip", "dripleafbig", "dripleaftall", "yoooooooominecraftsgotbigdrip");
        add(Material.SMALL_DRIPLEAF, "smalldrip", "dripleafsmall", "yoooooooominecraftsgotdrip");
        add(Material.POWDER_SNOW_BUCKET, "snowbucket", "snowpail");
        // "CUT_SANDSTONE" used to be called "SMOOTH_SANDSTONE"
        // "SMOOTH_SANDSTONE" is now a double slab
        add(Material.COBWEB, "spiderweb", "sweb", "cweb", "web");
        add(Material.IRON_BARS, "ironbarsb", "ironbarsblock", "ironfence", "metalbars", "metalbarsb", "metalbarsblock", "metalfence", "jailbars", "jailbarsb", "jailbarsblock", "jailfence", "mbars", "mbarsb", "mbarsblock", "mfence", "jbars", "jbarsb", "jbarsblock", "ibars", "ibarsb", "ibarsblock", "ifence");
        add(Material.ICE, "frozenwater", "waterfrozen", "freezewater", "waterfreeze");
        add(Material.HAY_BLOCK, "hay", "haybale", "baleofhay", "hayofbale");
        // == Plants (not crops) ==
        add(Material.TALL_GRASS, "longgrass", "wildgrass", "grasslong", "grasstall", "grasswild", "lgrass", "tgrass", "wgrass");
        add(Material.DEAD_BUSH, "bush", "deadshrub", "dshrub", "dbush", "deadsapling");
        add(Material.DANDELION, "yellowdandelion", "ydandelion", "yellowflower", "yflower", "flower");
        add(Material.POPPY, "rose", "redrose", "rrose", "redflower", "rflower", "poppy", "redpoppy");
        add(Material.BLUE_ORCHID, "cyanorchid", "lightblueorchid", "lblueorchid", "orchid", "cyanflower", "lightblueflower", "lblueflower");
        add(Material.ALLIUM, "magentaallium", "magentaflower");
        add(Material.AZURE_BLUET, "whiteazurebluet", "abluet", "azureb", "houstonia");
        add(Material.RED_TULIP, "tulipred", "rtulip", "tulipr");
        add(Material.WHITE_TULIP, "tulipwhite", "wtulip", "tulipw");
        add(Material.PINK_TULIP, "tulippink", "ptulip", "tulipp");
        add(Material.ORANGE_TULIP, "tuliporange", "otulip", "tulipo");
        add(Material.OXEYE_DAISY, "oxeye", "daisy", "daisyoxeye", "moondaisy", "daisymoon", "lightgrayoxeye", "lgrayoxeye", "lightgreyoxeye", "lgreyoxeye");
        add(Material.CACTUS, "cactuses", "cacti");
        add(Material.VINE, "vines", "greenvines", "greenvine", "gardenvines", "gardenvine", "vinesgreen", "vinegreen", "vinesgarden", "vinegarden", "vinesg", "vineg", "gvines", "gvine");
        add(Material.COCOA_BEANS, "cocoaplant", "cocoplant", "cplant", "cocoafruit", "cocofruit", "cfruit", "cocoapod", "cocopod", "cpod");
        add(Material.NETHER_SPROUTS, "nsprouts", "nethsprouts", "nsprout", "nethsprout", "nethersprout", "netherweed");
        add(Material.TWISTING_VINES, "twistvines", "twistvine");
        add(Material.WEEPING_VINES, "weepvines", "weepvine");
        add(Material.AZALEA, "azasmall", "azsmall", "babyazalea", "babyaza", "babyaz");
        add(Material.FLOWERING_AZALEA, "flowerazasmall", "flowerazsmall", "flowerbabyazalea", "flowerbabyaza", "flowerbabyaz", "flazasmall", "flazsmall", "flbabyazalea", "flbabyaza", "flbabyaz");
        add(Material.MOSS_BLOCK, "moss", "minecraftopensourcesoftware");
        add(Material.MOSS_CARPET, "mossfloor");
        add(Material.DECORATED_POT, "decorpot", "dflowerpot");
        add(Material.BAMBOO_BLOCK, "bamblock", "bbblock");
        add(Material.HANGING_ROOTS, "hangroot", "hangroots", "hangingroot", "hroots", "caveroot", "caveroots");
        // == Tools and Combat ==
        add(Material.SHIELD, "handshield", "woodshield", "woodenshield");
        add(Material.TOTEM_OF_UNDYING, "totem");
        add(Material.SPYGLASS, "magnifyingglass", "lens", "eyesgozoom");
        add(Material.RECOVERY_COMPASS, "recompass");
        // == Crops ==
        add(Material.CARVED_PUMPKIN, "hollowpumpkin", "cutpumpkin", "oldpumpkin", "legacypumpkin");
        add(Material.JACK_O_LANTERN, "pumpkinlantern", "glowingpumpkin", "lightpumpkin", "jpumpkin", "plantren", "glowpumpkin", "gpumpkin", "lpumpkin");
        add(Material.BEETROOT, "broot", "beet", "beets", "beetplant", "beetcrop");
        add(Material.BEETROOT_SEEDS, "beetrootseed", "brootseed", "brootseeds", "beetseed", "beetseeds", "beetsseeds", "beetplantseeds", "beetcropseeds");
        add(Material.MELON, "watermelon", "greenmelon", "melongreen", "melonblock", "watermelonblock", "greenmelonblock");
        add(Material.TORCHFLOWER, "tflower");
        add(Material.TORCHFLOWER_SEEDS, "torchseeds", "tflowerseeds", "tseeds");
        // == Food ==
        add(Material.BEETROOT_SOUP, "brootsoup", "beetsoup", "beetssoup", "beetplantsoup", "beetcropsoup", "redsoup");
        add(Material.GOLDEN_APPLE, "goldapple", "newgoldapple", "notnotchapple");
        add(Material.ENCHANTED_GOLDEN_APPLE, "notchapple", "godapple", "enchgoldapple");
        // == Froglights ==
        add(Material.OCHRE_FROGLIGHT, "ocfroglight", "ochrelight", "ofroglight", "olight");
        add(Material.PEARLESCENT_FROGLIGHT, "pearlfroglight", "pearllight", "pfroglight", "plight");
        add(Material.VERDANT_FROGLIGHT, "verdfroglight", "verdlight", "vfroglight", "vlight");
        // == End Materials ==
        add(Material.END_PORTAL, "endergoo", "enderportal", "endgoo", "eportal", "egoo");
        add(Material.END_PORTAL_FRAME, "endergooframe", "enderportalframe", "endgooframe", "eportalframe", "egooframe", "enderframe", "endframe");
        add(Material.END_STONE, "enderstone", "endrock", "enderrock", "erock", "estone");
        add(Material.DRAGON_EGG, "enderdragonegg", "endegg", "degg", "bossegg", "begg");
        add(Material.ELYTRA, "hangglider", "glider", "wings", "wing", "playerwings", "playerwing", "pwings", "pwing");
        add(Material.CHORUS_FRUIT, "chorus", "unpoppedchorus", "unpopchorus");
        add(Material.POPPED_CHORUS_FRUIT, "pchorus", "poppedchorus", "popchorus");
        add(Material.PHANTOM_MEMBRANE, "membrane", "superduperelytrarepairkit", "phmembrane", "pmembrane");
        // == Armor Trims ==
        add(Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, "coasttrim");
        add(Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, "dunetrim");
        add(Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, "eyetrim");
        add(Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, "hosttrim");
        add(Material.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, "raisertrim");
        add(Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, "ribtrim");
        add(Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, "sentrytrim");
        add(Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, "shapertrim");
        add(Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, "silencetrim");
        add(Material.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, "snouttrim");
        add(Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, "spiretrim");
        add(Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, "tidetrim");
        add(Material.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, "vextrim");
        add(Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, "wardtrim");
        add(Material.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, "wayfindertrim");
        add(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, "wildtrim");
        // == Pottery Sherds ==
        add(Material.SHEAF_POTTERY_SHERD, "sheafsherd");
        add(Material.ARCHER_POTTERY_SHERD, "archersherd");
        add(Material.ARMS_UP_POTTERY_SHERD, "armssherd", "armsupsherd");
        add(Material.BLADE_POTTERY_SHERD, "bladesherd");
        add(Material.BREWER_POTTERY_SHERD, "brewersherd", "brewsherd");
        add(Material.BURN_POTTERY_SHERD, "burnsherd");
        add(Material.DANGER_POTTERY_SHERD, "dangersherd");
        add(Material.EXPLORER_POTTERY_SHERD, "explorersherd");
        add(Material.FRIEND_POTTERY_SHERD, "friendsherd");
        add(Material.HEART_POTTERY_SHERD, "heartsherd");
        add(Material.HEARTBREAK_POTTERY_SHERD, "hbreaksherd", "heartbreaksherd", "heartbsherd");
        add(Material.HOWL_POTTERY_SHERD, "howlsherd");
        add(Material.MINER_POTTERY_SHERD, "minersherd");
        add(Material.MOURNER_POTTERY_SHERD, "mournersherd", "mournsherd");
        add(Material.PLENTY_POTTERY_SHERD, "plentysherd");
        add(Material.PRIZE_POTTERY_SHERD, "prizesherd");
        add(Material.SHEAF_POTTERY_SHERD, "sheafsherd");
        add(Material.SHELTER_POTTERY_SHERD, "sheltersherd");
        add(Material.SKULL_POTTERY_SHERD, "skullsherd");
        add(Material.SNORT_POTTERY_SHERD, "snortsherd");
        // == 1.20.5 Enum Renaming Manual Fixes ==
        add(Material.TURTLE_SCUTE, "scute", "minecraft:scute");
        add(EntityType.MOOSHROOM, "mushroom_cow_spawner");
        add(EntityType.SNOW_GOLEM, "snowman_spawner");
    }

    @Override
    public Stream<String> get(ItemProvider.Item item) {
        String[] names;
        if (item instanceof SpawnerProvider.SpawnerItem) {
            EntityType entity = ((SpawnerProvider.SpawnerItem) item).getEntity();
            names = FIXED_SPAWNER_ALIASES.get(entity);
        } else {
            names = FIXED_ALIASES.get(item.getMaterial());
        }

        if (names != null) {
            return Arrays.stream(names);
        }
        return null;
    }
}
