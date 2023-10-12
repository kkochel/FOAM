package pl.lodz.uni.biobank.foam.app.authentication;

public record SignInResponse(String token, String refreshToken) {
}
