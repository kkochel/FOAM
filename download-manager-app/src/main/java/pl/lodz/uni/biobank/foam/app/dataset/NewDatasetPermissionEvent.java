package pl.lodz.uni.biobank.foam.app.dataset;

public record NewDatasetPermissionEvent(
        String username,
        String datasetId
) {
}
