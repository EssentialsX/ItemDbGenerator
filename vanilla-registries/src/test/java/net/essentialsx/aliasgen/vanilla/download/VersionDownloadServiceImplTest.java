package net.essentialsx.aliasgen.vanilla.download;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class VersionDownloadServiceImplTest {

    @BeforeAll
    static void setup() throws IOException {
        Files.deleteIfExists(VersionDownloadServiceImpl.CACHE_DIRECTORY);
    }

    @Test
    void artifactCachePath() {
        Path actual;
        Path expected;

        expected = Paths.get(System.getProperty("user.home"), ".essx", "vanilla-cache", "artifacts", "21w08a", "server");
        actual = VersionDownloadServiceImpl.artifactCachePath("21w08a", "server");
        assertEquals(expected, actual, "Artifact cache path is incorrect");

        expected = Paths.get(System.getProperty("user.home"), ".essx", "vanilla-cache", "artifacts", "this_is", "weird_as_f");
        actual = VersionDownloadServiceImpl.artifactCachePath("this/is", "weird/as/f");
        assertEquals(expected, actual, "Escaped artifact cache path is incorrect");
    }
}
