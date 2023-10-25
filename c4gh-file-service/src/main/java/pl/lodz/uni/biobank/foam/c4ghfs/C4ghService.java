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

    private final Archive archive;
    private final Outbox outbox;

    public C4ghService(Archive archive, Outbox outbox) {
        this.archive = archive;
        this.outbox = outbox;
    }

    public void encryptionWithReceiverPublicKey(C4ghExportTask task, String privateKeyPath, String privateKeyPassword) throws IOException, GeneralSecurityException, DecoderException {
        log.info("Begin encryption of header from task with id: {}", task.taskId());
        InputStream file = archive.getFile(task.filePath());
        PrivateKey privateKey = KeyUtils.getInstance().readPrivateKey(new File(privateKeyPath), privateKeyPassword.toCharArray());
        PublicKey recipientPublicKey = KeyUtils.getInstance().readPublicKey(task.receiverPublicKey());
        byte[] newHeader = Crypt4GHUtils.getInstance().setRecipient(Hex.decodeHex(task.header()), privateKey, recipientPublicKey).serialize();
        log.info("Encryption of header has been finished for task with id: {}", task.taskId());

        InputStream outboxFile = new SequenceInputStream(new ByteArrayInputStream(newHeader), file);

        outbox.exportFile(outboxFile, task.taskId(), task.fileName(), task.username());
    }


}
