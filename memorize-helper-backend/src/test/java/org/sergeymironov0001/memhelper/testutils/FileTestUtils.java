package org.sergeymironov0001.memhelper.testutils;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@UtilityClass
public class FileTestUtils {

    public Path saveInputStreamToTmpFile(InputStream inputStream, String prefix, String suffix) throws IOException {
        Path tmpFile = Files.createTempFile(prefix, suffix);
        org.apache.commons.io.FileUtils.copyInputStreamToFile(inputStream, tmpFile.toFile());
        return tmpFile;
    }

    public InputStream getResourceFileAsStream(String fileName) {
        return FileTestUtils.class.getClassLoader().getResourceAsStream(fileName);
    }

    public Path getResourceFile(String fileName) throws URISyntaxException {
        return Paths.get(FileTestUtils.class.getClassLoader().getResource(fileName).toURI());
    }

    public boolean contentEquals(File file1, File file2) throws IOException {
        return org.apache.commons.io.FileUtils.contentEquals(file1, file2);
    }

    public boolean contentEquals(File originFile, InputStream inputStream) throws IOException {
    Path tmpFile = FileTestUtils.saveInputStreamToTmpFile(inputStream, "", "compare-files");
        boolean equals = FileTestUtils.contentEquals(originFile, tmpFile.toFile());
        Files.delete(tmpFile);
        return equals;
    }
}
