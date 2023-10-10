package pl.lodz.uni.biobank.foam.c4ghfs;

import java.io.IOException;
import java.io.InputStream;

public interface Outbox {

    void exportFile(InputStream outboxFile, String fileName, String username) throws IOException;
}
