package pl.lodz.uni.biobank.foam.c4ghfs;

import java.util.UUID;

public record FileExportEvent(
        String stableId,
        String username,
        UUID uuid,
        ExportStage exportStage
) {
}
