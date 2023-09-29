package pl.lodz.uni.biobank.foam.app.sda.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CegaUserMessage(
        @JsonProperty("keys")
        List<KeyMessage> keys,
        @JsonProperty("email")
        String email,
        @JsonProperty("username")
        String username,
        @JsonProperty("country")
        String country,
        @JsonProperty("full_name")
        String fullName,
        @JsonProperty("institution")
        String institution,
        @JsonProperty("password_hash")
        String passwordHash
) {
}
