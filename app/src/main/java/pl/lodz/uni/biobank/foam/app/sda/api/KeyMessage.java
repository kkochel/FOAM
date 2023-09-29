package pl.lodz.uni.biobank.foam.app.sda.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KeyMessage(
        @JsonProperty("type")
        String type,
        @JsonProperty("key")
        String key
) {
}
