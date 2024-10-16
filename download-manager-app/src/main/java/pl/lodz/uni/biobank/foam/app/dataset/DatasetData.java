package pl.lodz.uni.biobank.foam.app.dataset;

import pl.lodz.uni.biobank.foam.shared.FileData;

import java.util.List;

public record DatasetData(
        String stableId,
        String title,
        String description,
        List<FileData> datasetFiles
) {
}
