package pl.lodz.uni.biobank.foam.app.authentication;

public record AuthenticationResponse(String token, String refreshToken) {
}
