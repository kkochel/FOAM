package pl.lodz.uni.biobank.foam.app.authentication;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception logger to capture and log unexpected exceptions with full stack traces.
 * This helps locate the exact source of errors like NullPointerException in production logs.
 */
@ControllerAdvice
public class GlobalExceptionLoggingAdvice {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionLoggingAdvice.class);

    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleNullPointerException(HttpServletRequest request, NullPointerException ex) {
        logRequestWithStacktrace(request, ex, "Unhandled NullPointerException");
        return buildInternalServerErrorResponse();
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleGenericException(HttpServletRequest request, Exception ex) {
        logRequestWithStacktrace(request, ex, "Unhandled exception");
        return buildInternalServerErrorResponse();
    }

    private void logRequestWithStacktrace(HttpServletRequest request, Exception ex, String prefix) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        Principal principal = request.getUserPrincipal();
        String user = principal != null ? principal.getName() : "anonymous";
        String message = String.format("%s at %s %s?%s [user=%s]", prefix, method, uri, query == null ? "" : query, user);
        // Log with stacktrace
        log.error(message, ex);
    }

    private ResponseEntity<Map<String, Object>> buildInternalServerErrorResponse() {
        Map<String, Object> body = new HashMap<>();
        body.put("message", HttpStatus.I_AM_A_TEAPOT.getReasonPhrase());
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(body);
    }
}
