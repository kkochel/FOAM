package pl.lodz.uni.biobank.foam.app.export;

public record C4ghExportTask(
        String header,
        String filePath,
        String fileName,
        String receiverPublicKey,
        String username
) {
}
