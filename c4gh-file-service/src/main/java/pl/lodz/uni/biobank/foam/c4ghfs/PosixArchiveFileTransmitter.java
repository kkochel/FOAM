package pl.lodz.uni.biobank.foam.c4ghfs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class PosixArchiveFileTransmitter implements ArchiveFileTransmitter {
    private final String archivePath;

    public PosixArchiveFileTransmitter(String archivePath) {
        this.archivePath = archivePath;
    }

    @Override
    public InputStream getFile(String path) throws IOException {
        return Files.newInputStream(new File(archivePath, path).toPath());
    }
}
