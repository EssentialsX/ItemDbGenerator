package io.github.essentialsx.itemdbgenerator.providers.alias;

import io.github.essentialsx.itemdbgenerator.providers.item.ItemProvider;
import io.github.essentialsx.itemdbgenerator.providers.item.MaterialEnumProvider;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.bukkit.Material;

public class RecordAliasProvider extends CompoundAliasProvider {
    private static List<String> MUSIC_DISC_NAMES = Arrays.asList("musicrecord", "musicdisk", "musicdisc", "musiccd", "mrecord", "mdisk", "mdisc", "mcd", "record", "disk", "disc", "cd");

    @Override
    public Stream<String> get(ItemProvider.Item item) {
        if (!(item instanceof MaterialEnumProvider.MaterialEnumItem)) return null;

        Track track = Track.of(item.getMaterial());
        if (track == null) return null;

        return MUSIC_DISC_NAMES.stream()
            .flatMap(track::format);
    }

    private enum Track {
        THIRTEEN("MUSIC_DISK_13", "13%s", "gold%s", "go%s", "%s1", "1%s"),
        CAT(null, "cat%s", "green%s", "gr%s", "%s2", "2%s"),
        BLOCKS(null, "blocks%s", "orange%s", "or%s", "%s3", "3%s"),
        CHIRP(null, "chirp%s", "red%s", "re%s", "%s4", "4%s"),
        FAR(null, "far%s", "lightgreen%s", "lgreen%s", "lightgr%s", "lgr%s", "%s5", "5%s"),
        MALL(null, "mall%s", "purple%s", "pu%s", "%s6", "6%s"),
        MELLOHI(null, "mellohi%s", "pink%s", "pi%s", "%s7", "7%s"),
        STAL(null, "stal%s", "black%s", "bl%s", "%s8", "8%s"),
        STRAD(null, "strad%s", "white%s", "wh%s", "%s9", "9%s"),
        WARD(null, "ward%s", "darkgreen%s", "dgreen%s", "darkgr%s", "dgr%s", "%s10", "10%s"),
        ELEVEN("MUSIC_DISK_11", "11%s", "cracked%s", "crack%s", "c%s", "%s11"),
        WAIT(null, "wait%s", "blue%s", "cyan%s", "bl%s", "cy%s", "%s12", "12%s"),
        ;

        private final Pattern regex;
        private final String[] formats;

        Track(String regex, String... formats) {
            this.regex = getTypePattern(name(), regex);
            this.formats = getTypeFormats(name(), formats);
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

        public Stream<String> format(String record) {
            return Arrays.stream(formats)
                .map(format -> String.format(format, record));
        }
    }

}
