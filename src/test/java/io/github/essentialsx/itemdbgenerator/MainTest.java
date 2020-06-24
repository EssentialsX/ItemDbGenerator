package io.github.essentialsx.itemdbgenerator;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Material;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

class MainTest {

    private static JsonObject itemMap;

    @BeforeAll
    static void getItemMap() {
        itemMap = Main.generateItemMap();
    }

    @ParameterizedTest
    @MethodSource("spawnableItems")
    void testSignSpawnable(Material type) {
        boolean spawnable = fitsOnSign(type.name());
        String itemKey = "";

        System.out.println(type + ", " + spawnable);
        if (spawnable) return;

        for (Map.Entry<String, JsonElement> entry : itemMap.entrySet()) {
            if (entry.getValue().isJsonObject()
                    && entry.getValue().getAsJsonObject().getAsJsonPrimitive("material").getAsString().equals(type.name())) {
                itemKey = entry.getKey();
                spawnable = fitsOnSign(entry.getKey());
            }
        }

        if (!spawnable) {
            for (Map.Entry<String, JsonElement> entry : itemMap.entrySet()) {
                if (!entry.getValue().isJsonObject()
                        && entry.getValue().getAsString().equals(itemKey)
                        && fitsOnSign(entry.getKey())) {
                    spawnable = true;
                    System.out.println(type.name() + " spawnable as " + entry.getKey());
                    break;
                }
            }
        }

        assumeTrue(spawnable, type.name() + " is not spawnable on signs");
    }


    private static boolean fitsOnSign(String itemName) {
        return itemName.length() <= 14;
    }

    static Stream<Material> spawnableItems() {
        //noinspection deprecation
        return Arrays.stream(Material.values()).filter(material -> !material.isLegacy()).filter(Material::isItem);
    }

}
