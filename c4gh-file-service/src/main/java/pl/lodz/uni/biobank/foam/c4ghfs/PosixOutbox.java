package pl.lodz.uni.biobank.foam.c4ghfs;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PosixOutbox implements Outbox {
    private final String archivePath;

    public PosixOutbox(String archivePath) {
        this.archivePath = archivePath;
    }

    @Override
    public void exportFile(InputStream outboxFile, String fileName, String username) throws IOException {
        File file = new File(archivePath + "/" + username + "/" + new File(fileName).getName());
        if (!file.exists()) {
            FileUtils.copyToFile(outboxFile, file);
        }
        outboxFile.close();
    }
}
