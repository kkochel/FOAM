package pl.lodz.uni.biobank.foam.outbox;

import pl.lodz.uni.biobank.foam.shared.ExportStage;

import java.util.UUID;

public record FileExportMessage(
        String stableId,
        String username,
        UUID uuid,
        ExportStage exportStage
) {
}
