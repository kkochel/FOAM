package pl.lodz.uni.biobank.foam.outbox;

import java.util.UUID;

public record FileExportMessage(
        String stableId,
        String username,
        UUID uuid,
        ExportStage exportStage
) {
}
