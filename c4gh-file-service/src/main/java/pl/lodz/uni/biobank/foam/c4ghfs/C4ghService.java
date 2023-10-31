package pl.lodz.uni.biobank.foam.c4ghfs;

import no.uio.ifi.crypt4gh.util.Crypt4GHUtils;
import no.uio.ifi.crypt4gh.util.KeyUtils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class C4ghService {
    private static final Logger log = LoggerFactory.getLogger(C4ghService.class);

    private final ArchiveFileTransmitter archiveFileTransmitter;
    private final OutboxFileTransmitter outboxFileTransmitter;
    private final ExportStageSender stageSender;

    public C4ghService(ArchiveFileTransmitter archiveFileTransmitter, OutboxFileTransmitter outboxFileTransmitter, ExportStageSender stageSender) {
        this.archiveFileTransmitter = archiveFileTransmitter;
        this.outboxFileTransmitter = outboxFileTransmitter;
        this.stageSender = stageSender;
    }

    public void encryptionWithReceiverPublicKey(C4ghExportTask task, String privateKeyPath, String privateKeyPassword) {
        log.info("Begin encryption of header from task with id: {}", task.taskId());
        stageSender.handleSend(new FileExportEvent(task.fileId(), task.username(), task.taskId(), ExportStage.RE_ENCRYPTION));

        byte[] newHeader = encryptNewHeader(task, privateKeyPath, privateKeyPassword);
        log.info("Encryption of header has been finished for task with id: {}", task.taskId());

        InputStream exportFile = getExportFile(task);
        SequenceInputStream inputStream = new SequenceInputStream(new ByteArrayInputStream(newHeader), exportFile);
        outboxFileTransmitter.exportFile(inputStream, task);
    }

    private InputStream getExportFile(C4ghExportTask task) {
        try {
            return archiveFileTransmitter.getFile(task.filePath());
        } catch (IOException e) {
            stageSender.handleSend(new FileExportEvent(task.fileId(), task.username(), task.taskId(), ExportStage.FAILED));
            log.error("Unable get file for task: {}", task);
            throw new RuntimeException(e);
        }
    }

    private byte[] encryptNewHeader(C4ghExportTask task, String privateKeyPath, String privateKeyPassword) {
        try {
            PrivateKey privateKey = KeyUtils.getInstance().readPrivateKey(new File(privateKeyPath), privateKeyPassword.toCharArray());
            PublicKey recipientPublicKey = KeyUtils.getInstance().readPublicKey(task.receiverPublicKey());
            return Crypt4GHUtils.getInstance().setRecipient(Hex.decodeHex(task.header()), privateKey, recipientPublicKey).serialize();
        } catch (DecoderException | GeneralSecurityException | IOException e) {
            stageSender.handleSend(new FileExportEvent(task.fileId(), task.username(), task.taskId(), ExportStage.FAILED));
            log.error("Re-encryption has failed for task: {}", task);
            throw new RuntimeException(e);
        }
    }
}
