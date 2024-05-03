package io.github.essentialsx.itemdbgenerator.providers.item;

import io.github.essentialsx.itemdbgenerator.providers.util.AnnotationUtil;
import org.bukkit.Material;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class PotionProvider implements ItemProvider {
    private static final Material[] MATERIALS = {
            Material.POTION,
            Material.SPLASH_POTION,
            Material.LINGERING_POTION,
            Material.TIPPED_ARROW
    };

    private static final Map<PotionType, String> FALLBACK_NAMES = new HashMap<>();

    static {
        FALLBACK_NAMES.put(PotionType.LEAPING, "JUMP");
        FALLBACK_NAMES.put(PotionType.SWIFTNESS, "SPEED");
        FALLBACK_NAMES.put(PotionType.HEALING, "INSTANT_HEAL");
        FALLBACK_NAMES.put(PotionType.HARMING, "INSTANT_DAMAGE");
        FALLBACK_NAMES.put(PotionType.REGENERATION, "REGEN");
    }

    public static Stream<Item> getPotionsForType(PotionType type) {
        return Arrays.stream(MATERIALS)
                .map(material -> {
                    final String potionName = type.name();
                    if (potionName.startsWith("LONG_")) {
                        final PotionType normalizedType = getNormalizedPotion(potionName.substring(5));
                        return new PotionItem(material, normalizedType, FALLBACK_NAMES.get(normalizedType), false, true);
                    } else if (potionName.startsWith("STRONG_")) {
                        final PotionType normalizedType = getNormalizedPotion(potionName.substring(7));
                        return new PotionItem(material, normalizedType, FALLBACK_NAMES.get(normalizedType), true, false);
                    } else {
                        return new PotionItem(material, type, FALLBACK_NAMES.get(type), false, false);
                    }
                });
    }

    public static PotionType getNormalizedPotion(final String potionName) {
        return PotionType.valueOf(potionName);
    }

    @Override
    public Stream<Item> get() {
        return Arrays.stream(PotionType.values())
                .flatMap(PotionProvider::getPotionsForType);
    }

    public static class PotionItem extends Item {
        private final PotionData potionData;

        public PotionItem(Material material, PotionType type, String fallback, boolean upgraded, boolean extended) {
            super(material);
            potionData = new PotionData(type, fallback, upgraded, extended);
        }

        @Override
        public String getName() {
            return potionData.getMojangName() + "_" + getMaterial().name().toLowerCase();
        }

        public PotionData getPotionData() {
            return potionData;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            PotionItem that = (PotionItem) o;
            return potionData.equals(that.potionData);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), potionData);
        }
    }

    public static class PotionData {
        private final PotionType type;
        private final String fallbackType;
        private final boolean upgraded;
        private final boolean extended;

        PotionData(PotionType type, boolean upgraded, boolean extended) {
            this.type = type;
            this.fallbackType = null;
            this.upgraded = upgraded;
            this.extended = extended;
        }

        PotionData(PotionType type, String fallbackType, boolean upgraded, boolean extended) {
            this.type = type;
            this.fallbackType = fallbackType;
            this.upgraded = upgraded;
            this.extended = extended;
        }

        public PotionType getType() {
            return type;
        }

        public boolean isUpgraded() {
            return upgraded;
        }

        public boolean isExtended() {
            return extended;
        }

        public String getMojangName() {
            String baseName = type.name().toLowerCase();

            if (isExtended()) {
                return "long_" + baseName;
            } else if (isUpgraded()) {
                return "strong_" + baseName;
            }

            return baseName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PotionData that = (PotionData) o;
            return upgraded == that.upgraded &&
                    extended == that.extended &&
                    type == that.type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, upgraded, extended);
        }
    }
}
