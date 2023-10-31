package pl.lodz.uni.biobank.foam.app.authentication;

import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExpiredJwtExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ExpiredJwtExceptionHandler.class);

    @ExceptionHandler({ExpiredJwtException.class})
    @ResponseBody
    public ResponseEntity<HttpStatus> handleAuthenticationException(ExpiredJwtException ex) {
        log.info("JWT expired: {}", ex.toString());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
