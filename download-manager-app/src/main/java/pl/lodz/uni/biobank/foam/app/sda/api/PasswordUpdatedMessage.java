package pl.lodz.uni.biobank.foam.app.sda.api;

public record PasswordUpdatedMessage(
        String type,
        String user,
        String passwordHash
) {
}
