package pl.lodz.uni.biobank.foam.outbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;

@Service
public class FileStatusService {
    private static final Logger log = LoggerFactory.getLogger(FileStatusService.class);

    private final FileStatusSender fileStatusSender;

    public FileStatusService(FileStatusSender fileStatusSender) {
        this.fileStatusSender = fileStatusSender;
    }

    public void removeFile(String username, Path dstPath) {
        File file = dstPath.toFile();
        log.info("File {} affected by user {}. Operation type: {}", dstPath, username, Operation.REMOVE.name());
        FileExportMessage fem = new FileExportMessage(file.getName(), username, null, ExportStage.DELETED);
        fileStatusSender.handleSend(fem);
    }
}
