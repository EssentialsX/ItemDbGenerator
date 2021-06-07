package net.essentialsx.aliasgen.vanilla.download;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;


public class VersionDownloadServiceImpl implements VersionDownloadService {
    static final String VERSION_MANIFEST_URL = "https://launchermeta.mojang.com/mc/game/version_manifest.json";
    static final Path CACHE_DIRECTORY = Paths.get(System.getProperty("user.home"), ".essx", "vanilla-cache");
    static final Path MANIFEST_CACHE = CACHE_DIRECTORY.resolve("version_manifest.json");

    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private @MonotonicNonNull VersionManifest manifest = null;

    static Path artifactCachePath(String versionId, String artifactName) {
        return CACHE_DIRECTORY
                .resolve("artifacts")
                .resolve(versionId.replace('/', '_'))
                .resolve(artifactName.replace('/', '_'));
    }

    private void retrieveVersionManifest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VERSION_MANIFEST_URL))
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();

        HttpResponse<Path> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofFile(
                        Files.createTempFile("version_manifest", ".json.tmp")
                ));
        Path temp = response.body();

        Files.move(temp, MANIFEST_CACHE, StandardCopyOption.REPLACE_EXISTING);
    }

    private VersionDetails retrieveVersionDetails(final String versionMetaUrl) throws IOException, InterruptedException {
        // TODO: cache per-version meta? (or don't, in case of re-release?)
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(versionMetaUrl))
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();

        return this.mapper.readValue(
                this.httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray()).body(),
                VersionDetails.class
        );
    }

    private void loadVersionManifest() throws IOException, InterruptedException {
        if (Files.exists(MANIFEST_CACHE)) {
            try {
                final Instant modified = Files.getLastModifiedTime(MANIFEST_CACHE).toInstant();
                if (modified.isBefore(Instant.now().minus(30, ChronoUnit.MINUTES))) {
                    this.retrieveVersionManifest();
                }
            } catch (Exception ignored) {
                // we'll use an old manifest
            }
        } else {
            this.retrieveVersionManifest();
        }

        this.manifest = this.mapper.readValue(Files.newBufferedReader(MANIFEST_CACHE), VersionManifest.class);
    }

    private Path retrieveArtifact(VersionDetails version, String artifactName)
            throws IOException, InterruptedException {
        VersionDetails.ArtifactDetails artifactDetails = Objects.requireNonNull(
                version.downloads().get(artifactName),
                "Invalid artifact name " + artifactName + "!"
        );

        URI uri = URI.create(artifactDetails.url());
        Path destination = artifactCachePath(version.id(), artifactName);

        if (Files.exists(destination)) {
            // todo: check sha1 against download details
            return destination;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(Duration.ofMinutes(3))
                .GET()
                .build();

        this.httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofFile(destination)
        );

        return destination;
    }

    @Override
    public VersionManifest manifest() {
        if (this.manifest == null) {
            try {
                this.loadVersionManifest();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Failed to download version manifest!", e);
            }
        }

        return this.manifest;
    }

    @Override
    public VersionDetails details(VersionManifest.VersionSummary versionSummary) {
        try {
            return this.retrieveVersionDetails(versionSummary.url());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to download version details for version " + versionSummary.id() + "!", e);
        }
    }

    @Override
    public Path artifact(VersionDetails version, String artifactName) {
        try {
            return this.retrieveArtifact(version, artifactName);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to download artifact \"" + artifactName + "\" for version " + version.id() + "!", e);
        }
    }
}
