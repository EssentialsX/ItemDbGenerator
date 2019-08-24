package io.github.essentialsx.itemdbgenerator.providers.alias;

import com.google.common.collect.ObjectArrays;
import io.github.essentialsx.itemdbgenerator.providers.item.ItemProvider;
import io.github.essentialsx.itemdbgenerator.providers.item.SpawnerProvider;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class MobAliasProvider extends CompoundAliasProvider {
    @Override
    public Stream<String> get(ItemProvider.Item item) {
        MobType mobType = null;
        MobItemType itemType = MobItemType.of(item.getMaterial());

        if (item instanceof SpawnerProvider.SpawnerItem) {
            try {
                mobType = MobType.valueOf(((SpawnerProvider.SpawnerItem) item).getEntity().name());
            } catch (Exception ignored) {}
        }

        if (mobType == null) {
            mobType = MobType.of(item.getMaterial());
        }

        System.err.print("MOB-" + mobType + "-" + itemType + " ");

        if (mobType == null || itemType == null) return null;

        return getAliases(mobType, itemType);
    }

    private Stream<String> getAliases(MobType mobType, MobItemType itemType) {
        return Arrays.stream(mobType.names)
            .flatMap(itemType::format);
    }

    public static boolean isSpawnable(final EntityType type) {
        try {
            MobType.valueOf(type.name());
            return type.isSpawnable();
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * Represents mobs that can be spawned in the game.
     */
    @SuppressWarnings("unused")
    private enum MobType {
        ELDER_GUARDIAN("eguardian"),
        WITHER_SKELETON("wskeleton", "withersk", "wsk", "wither"),
        STRAY,
        HUSK,
        ZOMBIE_VILLAGER("zvillager", "deadvillager", "dvillager", "zvill", "dvill"),
        SKELETON_HORSE("skhorse", "shorse", "bonehorse"),
        ZOMBIE_HORSE("zhorse", "deadhorse", "dhorse"),
        DONKEY,
        MULE,
        EVOKER,
        VEX,
        VINDICATOR("vind"),
        ILLUSIONER,
        CREEPER("cr"),
        SKELETON("sk"),
        SPIDER("spid"),
        GIANT,
        PIG_ZOMBIE("zombiepigman", "zpigman", "pigman", "zombiepman", "zpman", "zombiepigm", "zpigm", "zombiepig", "zpig", "zombiepm", "zombiep", "zombiepigmen", "zpigmen", "pigmen"),
        ZOMBIE_PIGMAN(PIG_ZOMBIE),
        ZOMBIE("z", "zomb"),
        SLIME,
        GHAST,
        ENDERMAN,
        CAVE_SPIDER("cspider", "cspid"),
        SILVERFISH("sfish"),
        BLAZE,
        MAGMA_CUBE("lavaslime", "lavacube", "magmaslime"),
        ENDER_DRAGON("dragon", "edragon"),
        WITHER,
        BAT,
        WITCH,
        ENDERMITE("emite"),
        GUARDIAN("guard"),
        SHULKER("shulk"),
        PIG,
        SHEEP,
        MUSHROOM_COW("mooshroom", "mushroom"),
        MOOSHROOM(MUSHROOM_COW),
        COW,
        CHICKEN,
        SQUID,
        WOLF("dog"),
        SNOWMAN("snowgolem", "sgolem"),
        OCELOT("wildcat"),
        IRON_GOLEM("igolem"),
        HORSE,
        RABBIT,
        POLAR_BEAR("polar"),
        LLAMA,
        PARROT,
        VILLAGER("vill"),
        TURTLE,
        PHANTOM,
        COD,
        SALMON,
        PUFFERFISH("puffer", "pfish"),
        TROPICAL_FISH("tfish"),
        DROWNED,
        DOLPHIN("ecco"),
        CAT,
        PANDA,
        PILLAGER,
        RAVAGER,
        TRADER_LLAMA("tllama"),
        WANDERING_TRADER("trader"),
        FOX,
        PLAYER
        ;

        private final String[] names;

        MobType(String... names) {
            this.names = ObjectArrays.concat(name().toLowerCase(), names);
        }

        MobType(MobType otherType) {
            this.names = otherType.names;
        }

        public static MobType of(Material material) {
            String matName = material.name();
            for (MobType type : values()) {
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
    private enum MobItemType {
        SPAWNER(null, "%smobspawner", "%smobcage", "%smonsterspawner", "%smonstercage", "%smspawner", "%smcage", "%sspawner", "%scage"),
        SPAWN_EGG(null, "%segg", "egg%s", "%sspawnegg", "spawnegg%s", "%sspawn", "spawn%s"),
        SKULL(null, "%shead", "%sskull", "head%s", "%smask", "%sheadmask"),
        BUCKET(null, "%sbucket", "%sbukkit", "bucketo%s", "%spail"),
        ;

        private final Pattern regex;
        private final String[] formats;

        MobItemType(String regex, String... formats) {
            this.regex = getTypePattern(name(), regex);
            this.formats = getTypeFormats(name(), formats);
        }

        public static MobItemType of(Material material) {
            String matName = material.name();

            for (MobItemType type : values()) {
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
