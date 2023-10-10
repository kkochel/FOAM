package pl.lodz.uni.biobank.foam.app.sda.api;

import java.util.List;

public record CegaUserMessage(
        List<KeyMessage> keys,
        String email,
        String username,
        String country,
        String fullName,
        String institution,
        String passwordHash
) {
}
