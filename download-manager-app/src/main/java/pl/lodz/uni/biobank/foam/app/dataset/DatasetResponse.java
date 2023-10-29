package pl.lodz.uni.biobank.foam.app.dataset;

public record DatasetResponse(String stableId,
                              String title,
                              String description,
//                              List<FilesResponse> files,
                              String status) {
}
