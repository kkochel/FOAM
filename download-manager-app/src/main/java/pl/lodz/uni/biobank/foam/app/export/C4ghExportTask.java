package pl.lodz.uni.biobank.foam.app.export;

import java.util.UUID;

public record C4ghExportTask(
        UUID taskId,
        String header,
        String filePath,
        String fileName,
        String receiverPublicKey,
        String username,
        String stableId
) {
}
