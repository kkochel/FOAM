package pl.lodz.uni.biobank.foam.integration;

public record FileData(
        String stableId,
        String fileName,
        String archiveFilePath,
        Long archiveFileSize,
        Long decryptedFileSize,
        String header
) {
}
