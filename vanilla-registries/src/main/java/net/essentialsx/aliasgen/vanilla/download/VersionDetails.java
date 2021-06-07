package net.essentialsx.aliasgen.vanilla.download;

import java.util.Map;

public record VersionDetails(String id, Map<String, ArtifactDetails> downloads) {

    public record ArtifactDetails(String sha1, long size, String url) {
    }

}
