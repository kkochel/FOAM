package pl.lodz.uni.biobank.foam.c4ghfs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class PosixArchive implements Archive {
    private final String archivePath;

    public PosixArchive(String archivePath) {
        this.archivePath = archivePath;
    }

    @Override
    public InputStream getFile(String path) throws IOException {
        return Files.newInputStream(new File(archivePath, path).toPath());
    }
}
