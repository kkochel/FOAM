package pl.lodz.uni.biobank.foam.app.authentication;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Exception handler for JWT-related exceptions.
 * Provides appropriate HTTP responses for different types of JWT exceptions.
 */
@ControllerAdvice
public class ExpiredJwtExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ExpiredJwtExceptionHandler.class);

    /**
     * Handles expired JWT tokens.
     * Returns 401 Unauthorized with a message indicating the token has expired.
     */
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleExpiredJwtException(ExpiredJwtException ex) {
        log.info("JWT expired for subject: {}", ex.getClaims().getSubject());

        Map<String, String> response = new HashMap<>();
        response.put("error", "Token expired");
        response.put("message", "Your session has expired. Please log in again.");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Handles malformed JWT tokens.
     * Returns 400 Bad Request with a message indicating the token is malformed.
     */
    @ExceptionHandler(MalformedJwtException.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleMalformedJwtException(MalformedJwtException ex) {
        log.warn("Malformed JWT: {}", ex.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid token");
        response.put("message", "The authentication token is malformed.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles JWT signature validation failures.
     * Returns 401 Unauthorized with a message indicating the token signature is invalid.
     */
    @ExceptionHandler(SignatureException.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleSignatureException(SignatureException ex) {
        log.warn("JWT signature validation failed: {}", ex.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid token");
        response.put("message", "The authentication token signature is invalid.");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Handles other JWT exceptions.
     * Returns 401 Unauthorized with a generic message.
     */
    @ExceptionHandler(JwtException.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleJwtException(JwtException ex) {
        log.warn("JWT exception: {}", ex.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("error", "Authentication failed");
        response.put("message", "An error occurred with the authentication token.");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Handles SecurityException which is thrown by our JwtService.
     * Returns 401 Unauthorized with the specific error message.
     */
    @ExceptionHandler(SecurityException.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleSecurityException(SecurityException ex) {
        log.warn("Security exception: {}", ex.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("error", "Authentication failed");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
