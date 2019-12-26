package io.github.essentialsx.itemdbgenerator.providers.alias;

import com.google.common.collect.ObjectArrays;
import io.github.essentialsx.itemdbgenerator.providers.item.ItemProvider;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.bukkit.Material;

public class MeatFishAliasProvider extends CompoundAliasProvider {

    private static final List<String> BLACKLIST = Arrays.asList("SPAWN_EGG", "BUCKET", "FOOT");

    @Override
    public Stream<String> get(ItemProvider.Item item) {
        if (isBlacklisted(item.getMaterial())) return null;

        Food food = Food.of(item.getMaterial());
        FoodModifier itemType = FoodModifier.of(item.getMaterial());

//        System.err.print("ANIMAL-" + food + "-" + itemType + " ");

        if (food == null || itemType == null) {
            return null;
        }

        return getAliases(food, itemType);
    }

    private boolean isBlacklisted(Material material) {
        for (String str : BLACKLIST) {
            if (material.name().contains(str)) return true;
        }
        return false;
    }

    private Stream<String> getAliases(Food food, FoodModifier modifier) {
        return Arrays.stream(food.names)
            .flatMap(modifier::format);
    }

    private enum Food {
        BEEF("steak", "cowmeat"),
        CHICKEN,
        COD("fish"),
        MUTTON("sheepmeat"),
        PORKCHOP("pork"),
        PUFFERFISH("pufffish", "fishpuff", "pfish", "fishp"),
        RABBIT("hare", "hasenpfeffer"),
        SALMON("salmonfish", "sfish", "fishs"),
        TROPICAL_FISH("clownfish", "nemo", "clfish", "fishcl", "nfish", "fishn", "tfish", "fisht")
        ;
        private final String[] names;

        Food(String... names) {
            this.names = ObjectArrays.concat(name().toLowerCase(), names);
        }

        public static Food of(Material material) {
            String matName = material.name();

            for (Food food : values()) {
                if (matName.contains(food.name())) {
                    return food;
                }
            }

            return null;
        }
    }

    private enum FoodModifier {
        COOKED("COOKED_[A-Z_]+", "%scooked", "%scook", "%sc", "%sgrilled", "%sgrill", "%sg", "%sroasted", "%sroast", "%sro", "%sbbq", "%stoasted", "cooked%s", "cook%s", "c%s", "grilled%s", "grill%s", "g%s", "roasted%s", "roast%s", "ro%s", "bbq%s", "toasted%s"),
        HIDE("[A-Z_]+_HIDE", "%shide", "%sskin", "%scoat", "%sfur"),
        RAW("^(?!COOKED_)[A-Z_]+", "raw%s", "ra%s", "uncooked%s", "plain%s", "%s"),
        ;

        private final Pattern regex;
        private final String[] formats;

        FoodModifier(String regex, String... formats) {
            this.regex = getTypePattern(name(), regex);
            this.formats = getTypeFormats(name(), formats);
        }

        public static FoodModifier of(Material material) {
            String matName = material.name();

            for (FoodModifier type : values()) {
                if (type.regex.matcher(matName).matches()) {
                    return type;
                }
            }

            return null;
        }

        public Stream<String> format(String food) {
            return Arrays.stream(formats)
                .map(format -> String.format(format, food));
        }
    }
}
