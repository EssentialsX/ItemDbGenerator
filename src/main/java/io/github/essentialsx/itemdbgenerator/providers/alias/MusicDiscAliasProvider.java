package io.github.essentialsx.itemdbgenerator.providers.alias;

import io.github.essentialsx.itemdbgenerator.providers.item.ItemProvider;
import io.github.essentialsx.itemdbgenerator.providers.item.MaterialEnumProvider;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class MusicDiscAliasProvider extends CompoundAliasProvider {
    private static final List<String> MUSIC_DISC_NAMES = Arrays.asList("musicrecord", "musicdisk", "musicdisc", "mdisc", "record", "disk", "disc", "cd");

    @Override
    public Stream<String> get(ItemProvider.Item item) {
        if (!(item instanceof MaterialEnumProvider.MaterialEnumItem)) return null;

        if (item.getMaterial().name().contains("DISC") && item.getMaterial().name().contains("5")) {
            Logger.getLogger("MusicDisc").info("wooyeah");
        }

        Track track = Track.of(item.getMaterial());
        DiscType type = DiscType.of(item.getMaterial());
        if (track == null || type == null) return null;

        return getAliases(track, type);
    }

    private enum DiscType implements CompoundType {
        // this is scuffed beyond belief
        DISC_FRAGMENT("^DISC_FRAGMENT_5", "discfrag", "fragment"),
        MUSIC_DISC("^MUSIC_DISC_[A-Z0-9]+", generateFormatsFromNames(MUSIC_DISC_NAMES))
        ;

        private final Pattern regex;
        private final String[] formats;

        DiscType(String regex, String... formats) {
            this.regex = CompoundType.generatePattern(name(), regex);
            this.formats = CompoundType.generateFormats(name(), formats);
        }

        private static String[] generateFormatsFromNames(List<String> names) {
            return names.stream()
                    .flatMap(name -> Stream.of(name + "%s", "%s" + name))
                    .toArray(String[]::new);
        }

        public static DiscType of(Material material) {
            String matName = material.name();

            for (DiscType type : values()) {
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

    private enum Track implements CompoundModifier {
        THIRTEEN("MUSIC_DISC_13", "13", "gold", "go", "1"),
        CAT("MUSIC_DISC_CAT", "cat", "green", "gr", "2"),
        BLOCKS("MUSIC_DISC_BLOCKS", "blocks", "orange", "or", "3"),
        CHIRP("MUSIC_DISC_CHIRP", "chirp", "red", "re", "4"),
        FAR("MUSIC_DISC_FAR", "far", "lightgreen", "lgreen", "lightgr", "lgr"), // previously 5, replaced by Music Disc 5 in 1.19
        MALL("MUSIC_DISC_MALL", "mall", "purple", "pu", "6"),
        MELLOHI("MUSIC_DISC_MELLOHI", "mellohi", "pink", "pi", "7"),
        STAL("MUSIC_DISC_STAL", "stal", "black", "bl", "8"),
        STRAD("MUSIC_DISC_STRAD", "strad", "white", "wh", "9"),
        WARD("MUSIC_DISC_WARD", "ward", "darkgreen", "dgreen", "darkgr", "dgr", "10"),
        ELEVEN("MUSIC_DISC_11", "11", "cracked", "cr", "11"),
        WAIT("MUSIC_DISC_WAIT", "wait", "blue", "cyan", "bl", "cy", "12"),
        PIGSTEP("MUSIC_DISC_PIGSTEP", "pigstep", "nether", "dark", "neth", "pig", "14", "lenaraineisawesome"),
        OTHERSIDE("MUSIC_DISC_OTHERSIDE", "otherside", "cave", "under", "deep", "other", "15", "lenaraineisstillawesome"),
        FIVE("(MUSIC_DISC|DISC_FRAGMENT)_5", "five", "wild", "5", "16")
        ;

        private final Pattern regex;
        private final String[] names;

        Track(String regex, String... names) {
            this.regex = CompoundType.generatePattern(name(), regex);
            this.names = CompoundType.generateFormats(name(), names);
        }

        public static Track of(Material material) {
            String matName = material.name();

            for (Track type : values()) {
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

}
