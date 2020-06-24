package io.github.essentialsx.itemdbgenerator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.essentialsx.itemdbgenerator.providers.alias.*;
import io.github.essentialsx.itemdbgenerator.providers.item.ItemProvider;
import io.github.essentialsx.itemdbgenerator.providers.item.MaterialEnumProvider;
import io.github.essentialsx.itemdbgenerator.providers.item.PotionProvider;
import io.github.essentialsx.itemdbgenerator.providers.item.SpawnerProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Main {

    public static final List<ItemProvider> itemProviders = Arrays.asList(
            new MaterialEnumProvider(),
            new SpawnerProvider(),
            new PotionProvider()
    );
    public static final List<AliasProvider> aliasProviders = Arrays.asList(
            new SimpleAliasProvider(),
            new PotionAliasProvider(),
            new ColourAliasProvider(),
            new WoodAliasProvider(),
            new MineableAliasProvider(),
            new RecordAliasProvider(),
            new MobAliasProvider(),
            new MeatFishAliasProvider(),
            new PrismarineAliasProvider(),
            new RailAliasProvider(),
            new MinecartAliasProvider(),
            new PistonAliasProvider(),
            new FixedAliasProvider()
    );
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Path outputPath = Paths.get(".", "items.json");
    private static final String HEADER = "#version: ${full.version}\n# This file is for internal EssentialsX usage.\n# We recommend using custom_items.yml to add custom aliases.\n";

    public static void main(String[] args) {
        System.err.println("Generating items.json...");

        JsonObject itemMap = generateItemMap();
        save(itemMap);

        System.err.printf("Finished generating items.json with %d entries%n", itemMap.entrySet().size());
    }

    static JsonObject generateItemMap() {
        SortedSet<ItemProvider.Item> items = getItems();
        JsonObject itemMap = new JsonObject();

        items.forEach(item -> {
            itemMap.add(item.getName(), gson.toJsonTree(item));
            getAliases(item).forEach(alias -> {
                if (itemMap.has(alias)) {
                    if (itemMap.get(alias).isJsonObject()) {
                        System.err.printf("Not overwriting %s: %s with %s%n", alias, itemMap.get(alias), item.getName());
                        return;
                    }
                    System.err.printf("Found conflicting alias %s for %s - overwriting with %s%n", alias, itemMap.get(alias), item.getName());
                }
                itemMap.addProperty(alias, item.getName());
            });
        });

        return itemMap;
    }

    static void save(JsonObject itemMap) {
        String output = HEADER + gson.toJson(itemMap);

        try {
            Files.deleteIfExists(outputPath);
            Files.write(outputPath, output.getBytes());
        } catch (IOException e) {
            System.err.println("Failed to save items.json!");
            e.printStackTrace();
        }

        System.out.println(output);
    }

    static SortedSet<ItemProvider.Item> getItems() {
        return itemProviders.parallelStream()
                .flatMap(ItemProvider::get)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    static SortedSet<String> getAliases(ItemProvider.Item item) {
        return aliasProviders.stream()
                .flatMap(provider -> provider.get(item))
                .collect(Collectors.toCollection(TreeSet::new));
    }
}
