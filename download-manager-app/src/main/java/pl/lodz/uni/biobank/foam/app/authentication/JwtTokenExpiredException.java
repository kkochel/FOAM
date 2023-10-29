package pl.lodz.uni.biobank.foam.app.authentication;

public class JwtTokenExpiredException extends RuntimeException {
    public JwtTokenExpiredException(Throwable cause) {
        super(cause);
    }
}
