package net.essentialsx.aliasgen.vanilla.download;

import java.nio.file.Path;

public interface VersionDownloadService {

    /**
     * Get the version manifest. This may return a locally-cached manifest, or if no suitable manifest is found then it
     * will download the manifest from launchermeta.
     */
    VersionManifest manifest();

    /**
     * Get details for the given version summary. This may return locally-cached details if available, else it will
     * download the manifest from launchermeta.
     *
     * @param versionSummary The version to retrieve details for.
     * @return Details for the given version.
     */
    VersionDetails details(VersionManifest.VersionSummary versionSummary);

    /**
     * Get the given download artifact for the given version.
     * <p>
     * This will return a cached artifact if it exists and if the checksum matches the server's
     * {@link VersionDetails.ArtifactDetails#sha1()}, else it will attempt to download the artifact.
     * <p>
     * If the given download artifact cannot be found in the version details, this method will throw a
     * {@link IllegalArgumentException}.
     */
    Path artifact(VersionDetails version, String downloadName);

    /**
     * Get the server jar for the given version.
     * <p>
     * This will return a cached artifact if it exists and if the checksum matches the server's
     * {@link VersionDetails.ArtifactDetails#sha1()}, else it will attempt to download the artifact.
     */
    default Path serverJar(VersionDetails version) {
        return artifact(version, "server");
    }

}
