package pl.lodz.uni.biobank.foam.c4ghfs;

import pl.lodz.uni.biobank.foam.shared.ExportStage;

import java.util.UUID;

public record FileExportEvent(
        String stableId,
        String username,
        UUID uuid,
        ExportStage exportStage
) {
}
