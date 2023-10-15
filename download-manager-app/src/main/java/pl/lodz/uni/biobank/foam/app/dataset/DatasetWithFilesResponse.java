package pl.lodz.uni.biobank.foam.app.dataset;

import java.util.List;

public record DatasetWithFilesResponse(String stableId,
                                       String title,
                                       String description,
                                       List<FilesResponse> files,
                                       String status) {
}
