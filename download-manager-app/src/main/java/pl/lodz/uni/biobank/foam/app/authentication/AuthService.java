package pl.lodz.uni.biobank.foam.app.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service for handling authentication operations.
 * Manages user authentication, token refresh, and authentication status checks.
 */
@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private static final String ACCESS_TOKEN_TYPE = "access";

    private final AuthenticationManager am;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.am = authenticationManager;
        this.jwtService = jwtService;
    }

    /**
     * Authenticates a user with username and password.
     * 
     * @param request the sign-in request containing username and password
     * @return authentication response with JWT tokens
     * @throws AuthenticationException if authentication fails
     */
    public AuthenticationResponse authenticate(SignInRequest request) {
        try {
            // Mask the password in logs
            log.info("Authentication attempt for user: {}", request.username());

            // Authenticate the user
            Authentication authenticate = am.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );

            // Set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authenticate);

            // Generate and return JWT tokens
            AuthenticationResponse response = jwtService.generateTokens(authenticate);
            log.info("Authentication successful for user: {}", request.username());

            return response;
        } catch (BadCredentialsException e) {
            log.warn("Authentication failed for user: {} - Bad credentials", request.username());
            throw e;
        } catch (LockedException e) {
            log.warn("Authentication failed for user: {} - Account locked", request.username());
            throw e;
        } catch (AuthenticationException e) {
            log.warn("Authentication failed for user: {} - {}", request.username(), e.getMessage());
            throw e;
        }
    }

    /**
     * Refreshes an authentication token.
     * 
     * @param refreshToken the refresh token
     * @return authentication response with new JWT tokens
     * @throws SecurityException if the refresh token is invalid
     */
    public AuthenticationResponse refreshToken(String refreshToken) {
        try {
            log.debug("Token refresh requested");
            AuthenticationResponse response = jwtService.refreshToken(refreshToken);
            log.info("Token refreshed successfully for user: {}", jwtService.getUsername(refreshToken));
            return response;
        } catch (Exception e) {
            log.warn("Token refresh failed: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Checks if a token is valid for authentication.
     * 
     * @param token the access token to validate
     * @return HTTP status indicating authentication status (OK or UNAUTHORIZED)
     */
    public HttpStatus isAuthenticated(String token) {
        if (token == null) {
            log.debug("Authentication check failed: token is null");
            return HttpStatus.UNAUTHORIZED;
        }

        try {
            // Validate that this is an access token
            jwtService.validate(token, ACCESS_TOKEN_TYPE);
            log.debug("Authentication check successful for user: {}", jwtService.getUsername(token));
            return HttpStatus.OK;
        } catch (Exception e) {
            log.debug("Authentication check failed: {}", e.getMessage());
            return HttpStatus.UNAUTHORIZED;
        }
    }
}
