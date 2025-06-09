package pl.lodz.uni.biobank.foam.app.authentication;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for authentication endpoints.
 * Handles user sign-in, token refresh, sign-out, and authentication status checks.
 */
@RestController
@RequestMapping("api/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private static final String CLEAR_SITE_DATA_HEADER = "Clear-Site-Data";

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    /**
     * Handles user sign-in requests.
     * 
     * @param request the sign-in request containing username and password
     * @param response the HTTP response to add cookies to
     * @return response entity with appropriate status and message
     */
    @PostMapping("sign-in")
    public ResponseEntity<Map<String, String>> signIn(
            @RequestBody SignInRequest request, 
            HttpServletResponse response) {
        try {
            // Don't log the username in production for privacy reasons
            log.debug("Sign-in attempt received");

            // Authenticate the user and get tokens
            AuthenticationResponse res = service.authenticate(request);

            // Add tokens as cookies
            response.addCookie(res.token());
            response.addCookie(res.refreshToken());

            // Return success response
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("status", "success");
            responseBody.put("message", "Authentication successful");

            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch (BadCredentialsException e) {
            // Handle invalid credentials
            log.debug("Sign-in failed: Bad credentials");

            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("status", "error");
            responseBody.put("message", "Invalid username or password");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        } catch (LockedException e) {
            // Handle locked accounts
            log.debug("Sign-in failed: Account locked");

            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("status", "error");
            responseBody.put("message", "Account is locked");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        } catch (AuthenticationException e) {
            // Handle other authentication exceptions
            log.debug("Sign-in failed: {}", e.getMessage());

            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("status", "error");
            responseBody.put("message", "Authentication failed");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }
    }

    /**
     * Refreshes authentication tokens.
     * 
     * @param refreshToken the refresh token from cookies
     * @param response the HTTP response to add new cookies to
     * @return response entity with appropriate status and message
     */
    @PostMapping("refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(
            @CookieValue(value = "refreshToken", required = false) String refreshToken, 
            HttpServletResponse response) {
        try {
            if (refreshToken == null) {
                log.debug("Token refresh failed: No refresh token provided");

                Map<String, String> responseBody = new HashMap<>();
                responseBody.put("status", "error");
                responseBody.put("message", "No refresh token provided");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
            }

            // Refresh the tokens
            AuthenticationResponse res = service.refreshToken(refreshToken);

            // Add new tokens as cookies
            response.addCookie(res.token());
            response.addCookie(res.refreshToken());

            // Return success response
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("status", "success");
            responseBody.put("message", "Token refreshed successfully");

            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch (Exception e) {
            // Handle token refresh failures
            log.debug("Token refresh failed: {}", e.getMessage());

            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("status", "error");
            responseBody.put("message", "Failed to refresh token");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }
    }

    /**
     * Handles user sign-out.
     * Clears cookies, site data, and security context.
     * 
     * @param response the HTTP response to add headers to
     * @return response entity with appropriate status
     */
    @PostMapping("sign-out")
    public ResponseEntity<Map<String, String>> signOut(HttpServletResponse response) {
        // Clear site data and cookies
        response.addHeader(CLEAR_SITE_DATA_HEADER, "\"cache\", \"cookies\"");

        // Clear security context
        SecurityContextHolder.clearContext();

        // Return success response
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("status", "success");
        responseBody.put("message", "Signed out successfully");

        log.debug("User signed out successfully");
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    /**
     * Checks if a user is authenticated.
     * 
     * @param token the access token from cookies
     * @return response entity with appropriate status
     */
    @GetMapping("is-authenticated")
    public ResponseEntity<Map<String, String>> isAuthenticated(
            @CookieValue(value = "token", required = false) String token) {
        HttpStatus status = service.isAuthenticated(token);

        Map<String, String> responseBody = new HashMap<>();
        if (status == HttpStatus.OK) {
            responseBody.put("status", "success");
            responseBody.put("authenticated", "true");
        } else {
            responseBody.put("status", "error");
            responseBody.put("authenticated", "false");
        }

        return ResponseEntity.status(status).body(responseBody);
    }
}
