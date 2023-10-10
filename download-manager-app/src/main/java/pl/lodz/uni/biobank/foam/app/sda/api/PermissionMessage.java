package pl.lodz.uni.biobank.foam.app.sda.api;

public record PermissionMessage(
        String type,
        CegaUserMessage user,
        String editedAt,
        String createdAt,
        String datasetId,
        String expiresAt
) {
}
