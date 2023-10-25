package pl.lodz.uni.biobank.foam.c4ghfs;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class PosixOutbox implements Outbox {
    private static final Logger log = LoggerFactory.getLogger(PosixOutbox.class);

    private final String archivePath;

    public PosixOutbox(String archivePath) {
        this.archivePath = archivePath;
    }

    @Override
    public void exportFile(InputStream outboxFile, UUID taskId, String fileName, String username) throws IOException {
        File file = new File(archivePath + "/" + username + "/" + new File(fileName).getName());
        if (!file.exists()) {
            log.info("Begin copy file to outbox of file from task with id: {}", taskId);
            FileUtils.copyToFile(outboxFile, file);
        } else {
            log.info("File with name {} already exist. End task with id: {}", fileName, taskId);
        }

        outboxFile.close();
        log.info("End copy file to outbox of file from task with id: {}", taskId);
    }
}
