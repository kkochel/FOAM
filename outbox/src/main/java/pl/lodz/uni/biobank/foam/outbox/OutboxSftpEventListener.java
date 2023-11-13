package pl.lodz.uni.biobank.foam.outbox;

import jakarta.annotation.PostConstruct;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.sftp.server.FileHandle;
import org.apache.sshd.sftp.server.Handle;
import org.apache.sshd.sftp.server.SftpEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

import static pl.lodz.uni.biobank.foam.outbox.Operation.REMOVE;

@Component
public class OutboxSftpEventListener implements SftpEventListener {
    private static final Logger log = LoggerFactory.getLogger(OutboxAuthenticator.class);

    private final FileStatusService fileStatusService;

    public OutboxSftpEventListener(FileStatusService fileStatusService) {
        this.fileStatusService = fileStatusService;
    }

    @PostConstruct
    public void init() {
        log.info("Initializing {}", this.getClass());
    }

    @Override
    public void initialized(ServerSession session, int version) {
        log.info("SFTP session initialized for user: {}", session.getUsername());
    }

    @Override
    public void destroying(ServerSession session) {
        log.info("SFTP session closed for user: {}", session.getUsername());
    }

    @Override
    public void blocked(ServerSession session, String remoteHandle, FileHandle localHandle, long offset, long length, int mask, Throwable thrown) {
        log.info("User {} blocked file: {}", session.getUsername(), localHandle.getFile());
    }

    @Override
    public void unblocked(ServerSession session, String remoteHandle, FileHandle localHandle, long offset, long length, Throwable thrown) {
        log.info("User {} unblocked file: {}", session.getUsername(), localHandle.getFile());
    }

    @Override
    public void removing(ServerSession session, Path path, boolean isDirectory) {
        log.info("User {}  started the process of deleting the file: {}", session.getUsername(), path);
    }

    @Override
    public void removed(ServerSession session, Path path, boolean isDirectory, Throwable thrown) {
        log.info("User {} removed entry: {}", session.getUsername(), path);
        fileStatusService.processFile(REMOVE, session.getUsername(), path);
    }

    @Override
    public void closing(ServerSession session, String remoteHandle, Handle localHandle) throws IOException {
        throw new IOException("Closing is prohibited");
    }

    @Override
    public void linking(ServerSession session, Path source, Path target, boolean symLink) throws IOException {
        throw new IOException("Linking is prohibited");
    }

    @Override
    public void creating(ServerSession session, Path path, Map<String, ?> attrs) throws IOException {
        throw new IOException("Creating is prohibited");
    }

    @Override
    public void moving(ServerSession session, Path srcPath, Path dstPath, Collection<CopyOption> opts) throws IOException {
        throw new IOException("Moving is prohibited");
    }

    @Override
    public void writing(ServerSession session, String remoteHandle, FileHandle localHandle, long offset, byte[] data, int dataOffset, int dataLen) throws IOException {
        throw new IOException("Uploading is prohibited");
    }

}
