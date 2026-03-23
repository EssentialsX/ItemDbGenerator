package io.github.essentialsx.itemdbgenerator;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Material;
import org.bukkit.potion.PotionType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class StandaloneMain {

    private static final String MANIFEST_URL = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json";
    private static final Gson GSON = new Gson();

    public static void main(String[] args) {
        String targetVersion = args.length > 0 ? args[0] : null;

        try {
            if (targetVersion == null) {
                JsonObject manifest = fetchJson(MANIFEST_URL).getAsJsonObject();
                targetVersion = manifest.getAsJsonObject("latest").get("snapshot").getAsString();
                System.out.println("Targeting latest release: " + targetVersion);
            }

            File cacheDir = new File("cache");
            if (!cacheDir.exists()) cacheDir.mkdirs();

            // Clean up libraries to avoid version conflicts
            File libDir = new File(cacheDir, "libraries");
            if (libDir.exists()) {
                System.out.println("Cleaning up old libraries...");
                deleteDirectory(libDir);
            }

            System.out.println("Downloading server jar for version " + targetVersion + "...");
            File serverJar = new File(cacheDir, "server-" + targetVersion + ".jar");
            if (!serverJar.exists()) {
                downloadServerJar(targetVersion, serverJar);
            }
            System.out.println("Using server jar: " + serverJar.getAbsolutePath());

            System.out.println("Running server jar to unpack libraries...");
            new ProcessBuilder("java", "-jar", serverJar.getName(), "--help")
                .directory(cacheDir)
                .start()
                .waitFor();

            File versionsDir = new File(cacheDir, "versions");
            File extractedServerJar = findServerJar(versionsDir);
            if (extractedServerJar == null) {
                throw new RuntimeException("Could not find unpacked server jar in " + versionsDir);
            }
            System.out.println("Found unpacked server jar: " + extractedServerJar);

            String classpath = buildClasspath(cacheDir, extractedServerJar);

            System.out.println("Running data generator...");
            new ProcessBuilder("java", "-cp", classpath, "net.minecraft.data.Main", "--reports")
                .directory(cacheDir)
                .inheritIO()
                .start()
                .waitFor();

            File reportFile = new File(cacheDir, "generated/reports/registries.json");
            if (!reportFile.exists()) {
                 throw new RuntimeException("Data generator failed to produce " + reportFile);
            }

            System.out.println("Parsing item registry from registries.json...");
            Set<String> items = parseItemRegistry(reportFile);
            System.out.println("Found " + items.size() + " valid items.");
            Main.VALID_ITEMS = items;

            System.out.println("Generating experimental JSONs...");
            AnnotationGenerator.main(new String[0]);

            System.out.println("Loading experimental data...");
            Reader reader = new InputStreamReader(Objects.requireNonNull(Main.class.getResourceAsStream("/experimental_materials.json")));
            Main.EXPERIMENTAL_MATERIALS = GSON.fromJson(reader, new TypeToken<Set<Material>>(){}.getType());
            reader.close();
            reader = new InputStreamReader(Objects.requireNonNull(Main.class.getResourceAsStream("/experimental_potions.json")));
            Main.EXPERIMENTAL_POTIONS = GSON.fromJson(reader, new TypeToken<Set<PotionType>>(){}.getType());
            reader.close();

            System.out.println("Generating items.json...");
            JsonObject itemMap = Main.generateItemMap();
            Main.save(itemMap);

            System.out.println("Generation complete!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteDirectory(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDirectory(f);
            }
        }
        file.delete();
    }

    private static void downloadServerJar(String version, File outputFile) throws IOException {
        JsonObject manifest = fetchJson(MANIFEST_URL).getAsJsonObject();
        com.google.gson.JsonArray versions = manifest.getAsJsonArray("versions");

        String versionUrl = null;
        for (com.google.gson.JsonElement v : versions) {
            JsonObject vObj = v.getAsJsonObject();
            if (vObj.get("id").getAsString().equals(version)) {
                versionUrl = vObj.get("url").getAsString();
                break;
            }
        }

        if (versionUrl == null) {
            throw new IllegalArgumentException("Version " + version + " not found in manifest.");
        }

        JsonObject versionMeta = fetchJson(versionUrl).getAsJsonObject();
        JsonObject downloads = versionMeta.getAsJsonObject("downloads");
        if (!downloads.has("server")) {
             throw new IllegalArgumentException("No server download found for version " + version);
        }

        String serverUrl = downloads.getAsJsonObject("server").get("url").getAsString();
        downloadFile(serverUrl, outputFile);
    }

    private static File findServerJar(File dir) throws IOException {
        if (!dir.exists()) return null;
        try (Stream<Path> stream = Files.walk(dir.toPath())) {
            return stream
                .map(Path::toFile)
                .filter(f -> f.getName().endsWith(".jar") && !f.getName().contains("bundler"))
                .findFirst()
                .orElse(null);
        }
    }

    private static String buildClasspath(File workingDir, File extractedServerJar) throws IOException {
        StringBuilder cp = new StringBuilder();
        cp.append(extractedServerJar.getAbsolutePath());

        Path libPath = new File(workingDir, "libraries").toPath();
        if (Files.exists(libPath)) {
            Files.walk(libPath)
                .filter(p -> p.toString().endsWith(".jar"))
                .forEach(p -> {
                    cp.append(File.pathSeparator);
                    cp.append(p.toAbsolutePath().toString());
                });
        }
        return cp.toString();
    }

    private static Set<String> parseItemRegistry(File registriesFile) throws IOException {
        try (Reader reader = new java.io.FileReader(registriesFile)) {
            JsonObject json = GSON.fromJson(reader, JsonObject.class);
            JsonObject itemRegistry = json.getAsJsonObject("minecraft:item");
            if (itemRegistry == null) {
                throw new RuntimeException("No minecraft:item registry found in " + registriesFile);
            }
            JsonObject entries = itemRegistry.getAsJsonObject("entries");
            if (entries == null) {
                throw new RuntimeException("No entries found in minecraft:item registry");
            }
            Set<String> items = new HashSet<>();
            for (String key : entries.keySet()) {
                if (key.startsWith("minecraft:")) {
                    items.add(key.substring(10));
                } else {
                    items.add(key);
                }
            }
            return items;
        }
    }

    private static com.google.gson.JsonElement fetchJson(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            return GSON.fromJson(reader, com.google.gson.JsonElement.class);
        }
    }

    private static void downloadFile(String urlString, File outputFile) throws IOException {
        URL url = new URL(urlString);
        try (InputStream in = url.openStream();
             FileOutputStream out = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}
