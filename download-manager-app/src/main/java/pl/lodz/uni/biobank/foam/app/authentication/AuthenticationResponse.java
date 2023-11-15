package pl.lodz.uni.biobank.foam.app.authentication;

import jakarta.servlet.http.Cookie;

public record AuthenticationResponse(Cookie token, Cookie refreshToken) {
}
