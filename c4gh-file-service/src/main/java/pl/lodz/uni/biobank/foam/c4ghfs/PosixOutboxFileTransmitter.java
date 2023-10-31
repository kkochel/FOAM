package pl.lodz.uni.biobank.foam.c4ghfs;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class PosixOutboxFileTransmitter implements OutboxFileTransmitter {
    private static final Logger log = LoggerFactory.getLogger(PosixOutboxFileTransmitter.class);

    private final String archivePath;
    private final ExportStageSender stageSender;

    public PosixOutboxFileTransmitter(String archivePath, ExportStageSender stageSender) {
        this.archivePath = archivePath;
        this.stageSender = stageSender;
    }

    @Override
    public void exportFile(InputStream outboxFile, C4ghExportTask task) {
        String fileName = task.fileName();

        File file = new File(archivePath + "/" + task.username() + "/" + task.datasetId() + "/" + new File(fileName).getName());
        if (!file.exists()) {
            copyFileFromArchiveToOutbox(outboxFile, task, file);
        } else {
            log.info("File with name {} already exist. End task with id: {}", fileName, task.taskId());
        }

        closeStream(outboxFile, task);
    }

    private void closeStream(InputStream outboxFile,C4ghExportTask task)  {
        UUID taskId = task.taskId();

        try {
            outboxFile.close();
        } catch (IOException e) {
            stageSender.handleSend(new FileExportEvent(task.fileId(), task.username(), task.taskId(), ExportStage.FAILED));
            log.error("Unable to close stream for task: {}", task);
            throw new RuntimeException(e);
        }
        log.info("End copy file to outbox of file from task with id: {}", taskId);
        stageSender.handleSend(new FileExportEvent(task.fileId(), task.username(), taskId, ExportStage.READY));
    }

    private void copyFileFromArchiveToOutbox(InputStream outboxFile, C4ghExportTask task, File file) {
        UUID taskId = task.taskId();

        log.info("Begin copy file to outbox of file from task with id: {}", taskId);
        stageSender.handleSend(new FileExportEvent(task.fileId(), task.username(), taskId, ExportStage.TRANSFER));
        try {
            FileUtils.copyToFile(outboxFile, file);
        } catch (IOException e) {
            stageSender.handleSend(new FileExportEvent(task.fileId(), task.username(), task.taskId(), ExportStage.FAILED));
            log.error("Unable to copy file for task: {}", task);
            throw new RuntimeException(e);
        }
    }
}
