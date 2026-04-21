
package pl.lodz.uni.biobank.foam.app.authentication;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Base64;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for JwtService
 * Tests only happy path scenarios
 */
@ExtendWith(MockitoExtension.class)
class JwtServiceTest {
    private JwtService jwtService;
    private final String secretKey = JwtService.generateSecureKey(64);
    private final long jwtExpiration = 900000; // 15 minutes
    private final long refreshExpiration = 10800000; // 3 hours
    private final String issuer = "test-issuer";
    private final String audience = "test-audience";
    private final String username = "testuser";

    @BeforeEach
    void setup() {
        jwtService = new JwtService(secretKey, jwtExpiration, refreshExpiration, issuer, audience);
    }

    /**
     * Tests for generateSecureKey method
     */
    @Test
    void generateSecureKeyShouldGenerateBase64EncodedStringOfSpecifiedLength() {
        // when
        String key = JwtService.generateSecureKey(32);

        // then
        assertThat(key).isNotNull();
        assertThat(key).isNotEmpty();
        assertThat(Base64.getDecoder().decode(key)).hasSize(32);
    }

    @Test
    void generateSecureKeyShouldGenerateDifferentKeysOnEachCall() {
        // when
        String key1 = JwtService.generateSecureKey(32);
        String key2 = JwtService.generateSecureKey(32);

        // then
        assertThat(key1).isNotEqualTo(key2);
    }

    /**
     * Tests for validate method
     */
    @Test
    void validateShouldAcceptValidTokens() throws Exception {
        // given
        String token = generateValidToken("access");

        // when/then
        assertThatCode(() -> jwtService.validate(token))
                .doesNotThrowAnyException();
    }

    @Test
    void validateWithTokenTypeShouldAcceptValidTokensWithCorrectType() throws Exception {
        // given
        String token = generateValidToken("access");

        // when/then
        assertThatCode(() -> jwtService.validate(token, "access"))
                .doesNotThrowAnyException();
    }

    /**
     * Tests for getUsername method
     */
    @Test
    void getUsernameShouldExtractUsernameFromToken() throws Exception {
        // given
        String token = generateValidToken("access");

        // when
        String extractedUsername = jwtService.getUsername(token);

        // then
        assertThat(extractedUsername).isEqualTo(username);
    }

    /**
     * Tests for refreshToken method
     */
    @Test
    void refreshTokenShouldCreateNewTokensWhenGivenValidRefreshToken() throws Exception {
        // given
        String refreshToken = generateValidToken("refresh");

        // when
        var response = jwtService.refreshToken(refreshToken);

        // then
        assertThat(response).isNotNull();
        assertThat(response.token()).isNotNull();
        assertThat(response.refreshToken()).isNotNull();

        // Verify the access token
        Cookie accessTokenCookie = response.token();
        assertThat(accessTokenCookie.getName()).isEqualTo("token");
        assertThat(accessTokenCookie.getMaxAge()).isEqualTo((int) (jwtExpiration / 1000));

        // Verify the refresh token
        Cookie refreshTokenCookie = response.refreshToken();
        assertThat(refreshTokenCookie.getName()).isEqualTo("refreshToken");
        assertThat(refreshTokenCookie.getMaxAge()).isEqualTo((int) (refreshExpiration / 1000));
    }

    /**
     * Tests for generateTokens method
     */
    @Test
    void generateTokensShouldCreateBothAccessAndRefreshTokens() throws Exception {
        // given
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = new UserDetailsImpl(username, "password");
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // when
        var response = jwtService.generateTokens(authentication);

        // then
        assertThat(response).isNotNull();
        assertThat(response.token()).isNotNull();
        assertThat(response.refreshToken()).isNotNull();

        // Verify the access token
        Cookie accessTokenCookie = response.token();
        assertThat(accessTokenCookie.getName()).isEqualTo("token");
        assertThat(accessTokenCookie.getMaxAge()).isEqualTo((int) (jwtExpiration / 1000));

        // Verify the refresh token
        Cookie refreshTokenCookie = response.refreshToken();
        assertThat(refreshTokenCookie.getName()).isEqualTo("refreshToken");
        assertThat(refreshTokenCookie.getMaxAge()).isEqualTo((int) (refreshExpiration / 1000));
    }

    /**
     * Helper methods for testing
     */
    private String generateValidToken(String tokenType) throws Exception {
        // Use reflection to access the private generateToken method
        java.lang.reflect.Method generateTokenMethod = JwtService.class.getDeclaredMethod(
                "generateToken", String.class, long.class, String.class);
        generateTokenMethod.setAccessible(true);
        return (String) generateTokenMethod.invoke(jwtService, username, 
                "refresh".equals(tokenType) ? refreshExpiration : jwtExpiration, tokenType);
    }

    private String generateTokenWithCustomExpiration(long expiration) throws Exception {
        // Use reflection to access the private generateToken method
        java.lang.reflect.Method generateTokenMethod = JwtService.class.getDeclaredMethod(
                "generateToken", String.class, long.class, String.class);
        generateTokenMethod.setAccessible(true);
        return (String) generateTokenMethod.invoke(jwtService, username, expiration, "refresh");
    }
}
