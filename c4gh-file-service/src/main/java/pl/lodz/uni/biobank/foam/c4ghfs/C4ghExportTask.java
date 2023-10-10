package pl.lodz.uni.biobank.foam.c4ghfs;

public record C4ghExportTask(
        String header,
        String filePath,
        String fileName,
        String receiverPublicKey,
        String username
) {
}
