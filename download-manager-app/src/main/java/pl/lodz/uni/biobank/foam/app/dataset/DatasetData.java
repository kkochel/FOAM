package pl.lodz.uni.biobank.foam.app.dataset;

import java.util.List;

public record DatasetData(
        String stableId,
        String title,
        String description,
        List<FileData> datasetFiles
) {
}
