package pl.lodz.uni.biobank.foam.integration;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DatasetRelease(
        @JsonProperty("type") String type,
        @JsonProperty("dataset_id") String datasetId
) {
}
