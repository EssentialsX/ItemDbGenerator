package io.github.essentialsx.itemdbgenerator;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.essentialsx.itemdbgenerator.providers.alias.AliasProvider;
import io.github.essentialsx.itemdbgenerator.providers.alias.ColourAliasProvider;
import io.github.essentialsx.itemdbgenerator.providers.alias.CopperBuildingBlockAliasProvider;
import io.github.essentialsx.itemdbgenerator.providers.alias.CrystalAliasProvider;
import io.github.essentialsx.itemdbgenerator.providers.alias.DeepFungiAliasProvider;
import io.github.essentialsx.itemdbgenerator.providers.alias.FixedAliasProvider;
import io.github.essentialsx.itemdbgenerator.providers.alias.MeatFishAliasProvider;
import io.github.essentialsx.itemdbgenerator.providers.alias.MineableAliasProvider;
import io.github.essentialsx.itemdbgenerator.providers.alias.MinecartAliasProvider;
import io.github.essentialsx.itemdbgenerator.providers.alias.MineralAliasProvider;
import io.github.essentialsx.itemdbgenerator.providers.alias.MobAliasProvider;
import io.github.essentialsx.itemdbgenerator.providers.alias.MusicDiscAliasProvider;
import io.github.essentialsx.itemdbgenerator.providers.alias.PistonAliasProvider;
import io.github.essentialsx.itemdbgenerator.providers.alias.PotionAliasProvider;
import io.github.essentialsx.itemdbgenerator.providers.alias.RailAliasProvider;
import io.github.essentialsx.itemdbgenerator.providers.alias.SimpleAliasProvider;
import io.github.essentialsx.itemdbgenerator.providers.alias.WoodAliasProvider;
import io.github.essentialsx.itemdbgenerator.providers.item.ItemProvider;
import io.github.essentialsx.itemdbgenerator.providers.item.MaterialEnumProvider;
import io.github.essentialsx.itemdbgenerator.providers.item.PotionProvider;
import io.github.essentialsx.itemdbgenerator.providers.item.SpawnerProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionType;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Main extends JavaPlugin {

    protected static final List<ItemProvider> itemProviders = Arrays.asList(
            new MaterialEnumProvider(),
            new SpawnerProvider(),
            new PotionProvider()
    );
    protected static final List<AliasProvider> aliasProviders = Arrays.asList(
            new SimpleAliasProvider(),
            new PotionAliasProvider(),
            new ColourAliasProvider(),
            new MineralAliasProvider(),
            new WoodAliasProvider(),
            new MineableAliasProvider(),
            new MusicDiscAliasProvider(),
            new MobAliasProvider(),
            new MeatFishAliasProvider(),
            new CrystalAliasProvider(),
            new RailAliasProvider(),
            new MinecartAliasProvider(),
            new PistonAliasProvider(),
            new DeepFungiAliasProvider(),
            new CopperBuildingBlockAliasProvider(),
            new FixedAliasProvider()
    );
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path OUTPUT_PATH = Paths.get(".", "items.json");
    private static final String HEADER = "#version: ${full.version}\n# This file is for internal EssentialsX usage.\n# We recommend using custom_items.yml to add custom aliases.\n";

    public static Set<Material> EXPERIMENTAL_MATERIALS = null;
    public static Set<PotionType> EXPERIMENTAL_POTIONS = null;

    @Override
    public void onEnable() {
        System.err.println("Generating items.json...");
        try {
            final Gson gson = new Gson();

            Reader reader = new InputStreamReader(Objects.requireNonNull(Main.class.getResourceAsStream("/experimental_materials.json")));
            EXPERIMENTAL_MATERIALS = gson.fromJson(reader, new TypeToken<Set<Material>>(){}.getType());
            reader.close();
            reader = new InputStreamReader(Objects.requireNonNull(Main.class.getResourceAsStream("/experimental_potions.json")));
            EXPERIMENTAL_POTIONS = gson.fromJson(reader, new TypeToken<Set<PotionType>>(){}.getType());
            reader.close();


            JsonObject itemMap = generateItemMap();
            save(itemMap);

            System.err.printf("Finished generating items.json with %d entries%n", itemMap.entrySet().size());
        } catch (Exception e) {
          //noinspection CallToPrintStackTrace
          e.printStackTrace();
        }
        Bukkit.shutdown();
    }

    static JsonObject generateItemMap() {
        SortedSet<ItemProvider.Item> items = getItems();
        JsonObject itemMap = new JsonObject();

        items.forEach(item -> {
            itemMap.add(item.getName(), GSON.toJsonTree(item));
            getAliases(item).forEach(alias -> {
                if (itemMap.has(alias)) {
                    if (itemMap.get(alias).isJsonObject()) {
                        // Don't log aliases if they match the item name
                        if (!item.getName().equalsIgnoreCase(alias)) {
                            System.err.printf("Not overwriting %s: %s with %s%n", alias, itemMap.get(alias), item.getName());
                        }
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
        String output = HEADER + GSON.toJson(itemMap);

        try {
            Files.deleteIfExists(OUTPUT_PATH);
            Files.write(OUTPUT_PATH, output.getBytes());
            System.err.println("Saved items.json successfully.");
        } catch (IOException e) {
            System.err.println("Failed to save items.json! Dumping items.json:");
            System.out.println(output);
            e.printStackTrace();
        }
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
