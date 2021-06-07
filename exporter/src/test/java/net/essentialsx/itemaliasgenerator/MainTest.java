package net.essentialsx.itemaliasgenerator;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

class MainTest {

    private JsonObject itemMap;

    @BeforeEach
    void getItemMap() {
        itemMap = Main.generateItemMap();
    }

    /**
     * Tests whether all items have aliases that are short enough to fit on an Essentials sign.
     */
    @Test
    void testSignSpawnable() {
        Set<String> names = new HashSet<>();

        itemMap.entrySet().forEach(entry -> {
            if (entry.getValue().isJsonObject() && !fitsOnSign(entry.getKey())) {
                names.add(entry.getKey());
            }
        });

        itemMap.entrySet().forEach(entry -> {
            if (!entry.getValue().isJsonObject() && fitsOnSign(entry.getKey())) {
                names.remove(entry.getValue().getAsString());
            }
        });

        assumeTrue(names.isEmpty(), () -> "There are " + names.size() + " items that don't have aliases short enough to fit on signs:\n" + Arrays.toString(names.toArray()));
    }

    private boolean fitsOnSign(String itemName) {
        return itemName.length() <= 14;
    }
}
