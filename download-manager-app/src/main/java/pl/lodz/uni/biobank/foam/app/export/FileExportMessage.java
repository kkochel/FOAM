package pl.lodz.uni.biobank.foam.app.export;

import java.util.UUID;

public record FileExportMessage(
        String stableId,
        String username,
        UUID uuid,
        ExportStage exportStage
) {
}
