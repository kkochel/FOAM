package pl.lodz.uni.biobank.foam.c4ghfs;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public interface Outbox {

    void exportFile(InputStream outboxFile, UUID taskId, String fileName, String username) throws IOException;
}
