package pl.lodz.uni.biobank.foam.app.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class JwtService {
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    private static final String REFRESH_TOKEN_NAME = "refreshToken";
    private static final String TOKEN_NAME = "token";
    private static final String TOKEN_TYPE_CLAIM = "token_type";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    private final String applicationKey;
    private final long jwtExpiration;
    private final long refreshExpiration;
    private final String issuer;
    private final String audience;

    public JwtService(String applicationKey, long jwtExpiration, long refreshExpiration, String issuer, String audience) {
        this.applicationKey = applicationKey;
        this.jwtExpiration = jwtExpiration;
        this.refreshExpiration = refreshExpiration;
        this.issuer = issuer;
        this.audience = audience;
    }

    public AuthenticationResponse generateTokens(Authentication authenticate) {
        UserDetailsImpl user = (UserDetailsImpl) authenticate.getPrincipal();
        return new AuthenticationResponse(
            generateCookie(user, jwtExpiration, TOKEN_NAME, ACCESS_TOKEN_TYPE),
            generateCookie(user, refreshExpiration, REFRESH_TOKEN_NAME, REFRESH_TOKEN_TYPE)
        );
    }

    public AuthenticationResponse refreshToken(String refreshToken) {
        validate(refreshToken, REFRESH_TOKEN_TYPE);
        String username = getUsername(refreshToken);
        return new AuthenticationResponse(
            generateCookie(username, jwtExpiration, TOKEN_NAME, ACCESS_TOKEN_TYPE),
            generateCookie(username, refreshExpiration, REFRESH_TOKEN_NAME, REFRESH_TOKEN_TYPE)
        );
    }

    public void validate(String token) {
        validate(token, null);
    }

    public void validate(String token, String expectedTokenType) {
        if (token == null) {
            log.warn("Token validation failed: token is null");
            throw new SecurityException("Token is null");
        }

        try {
            Claims claims = Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();

            // Validate token type if expected type is provided
            if (expectedTokenType != null) {
                String tokenType = claims.get(TOKEN_TYPE_CLAIM, String.class);
                if (!expectedTokenType.equals(tokenType)) {
                    log.warn("Token validation failed: invalid token type. Expected: {}, Actual: {}", 
                        expectedTokenType, tokenType);
                    throw new SecurityException("Invalid token type");
                }
            }

            // Validate audience and issuer
            if (!claims.getAudience().contains(audience)) {
                log.warn("Token validation failed: invalid audience");
                throw new SecurityException("Invalid token audience");
            }

            if (!issuer.equals(claims.getIssuer())) {
                log.warn("Token validation failed: invalid issuer");
                throw new SecurityException("Invalid token issuer");
            }

        } catch (JwtException e) {
            log.warn("Token validation failed: {}", e.getMessage());
            throw new SecurityException("Invalid token: " + e.getMessage());
        }
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private Cookie generateCookie(UserDetails userDetails, long jwtExpiration, String name, String tokenType) {
        return generateCookie(userDetails.getUsername(), jwtExpiration, name, tokenType);
    }

    private Cookie generateCookie(String userName, long jwtExpiration, String name, String tokenType) {
        Cookie cookie = new Cookie(name, generateToken(userName, jwtExpiration, tokenType));
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) jwtExpiration / 1000);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "Strict");
        return cookie;
    }

    private String generateToken(String username, long jwtExpiration, String tokenType) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .notBefore(now)
                .expiration(expiryDate)
                .id(UUID.randomUUID().toString()) // Add jti claim for token revocation capability
                .audience().add(audience).and()
                .issuer(issuer)
                .claim(TOKEN_TYPE_CLAIM, tokenType)
                .signWith(key(), SignatureAlgorithm.HS512) // Explicitly specify algorithm
                .compact();
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(applicationKey));
    }

    /**
     * Generates a secure random key for use as JWT secret
     * This method can be used to generate a secure key for production use
     * 
     * @param keyLength the length of the key in bytes
     * @return Base64 encoded secure random key
     */
    public static String generateSecureKey(int keyLength) {
        byte[] key = new byte[keyLength];
        new SecureRandom().nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
}
