package pl.lodz.uni.biobank.foam.app.sda.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PermissionMessage(
        @JsonProperty("type")
        String type,
        @JsonProperty("user")
        CegaUserMessage user,
        @JsonProperty("edited_at")
        String editedAt,
        @JsonProperty("created_at")
        String createdAt,
        @JsonProperty("dataset_id")
        String datasetId,
        @JsonProperty("expires_at")
        String expiresAt
) {
}
