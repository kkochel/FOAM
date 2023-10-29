package pl.lodz.uni.biobank.foam.c4ghfs;

import java.io.InputStream;

public interface OutboxFileTransmitter {

    void exportFile(InputStream outboxFile, C4ghExportTask task);
}
