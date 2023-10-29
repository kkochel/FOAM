package pl.lodz.uni.biobank.foam.c4ghfs;

import java.io.IOException;
import java.io.InputStream;

public interface ArchiveFileTransmitter {

    InputStream getFile(String path) throws IOException;
}
