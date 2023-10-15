package pl.lodz.uni.biobank.foam.app.sda.api;

public record PermissionDeletedMessage(
        String type,
        String user,
        String datasetId
) {
}
