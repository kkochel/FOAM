package pl.lodz.uni.biobank.foam.app.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class JwtTokenExpiredExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(JwtTokenExpiredExceptionHandler.class);

    @ExceptionHandler({BadCredentialsException.class})
    @ResponseBody
    public ResponseEntity<HttpStatus> handleAuthenticationException(Exception ex) {

        log.info("SignIn failed reason {}", ex.toString());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
