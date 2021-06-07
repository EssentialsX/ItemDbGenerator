package net.essentialsx.aliasgen.vanilla.download;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Map;

public record VersionManifest(Map<Branch, String> latest, List<VersionSummary> versions) {

    public @Nullable String metaUrlFor(final String versionId) {
        for (VersionManifest.VersionSummary version : this.versions) {
            if (version.id().equals(versionId)) {
                return version.url();
            }
        }

        return null;
    }

    enum Branch {
        RELEASE,
        SNAPSHOT,
    }

    public record VersionSummary(String id, Branch type, String url) {
    }

}
