package pl.lodz.uni.biobank.foam.app.authentication;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    @Value("${application.jwt.secret-key}")
    private String applicationKey;
    @Value("${application.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.jwt.refresh-token.expiration}")
    private long refreshExpiration;


    public AuthenticationResponse generateTokens(Authentication authenticate) {
        UserDetailsImpl user = (UserDetailsImpl) authenticate.getPrincipal();
        return new AuthenticationResponse(generateToken(user, jwtExpiration), generateToken(user, refreshExpiration));
    }

    public AuthenticationResponse refreshToken(String refreshToken) {
        validate(refreshToken);
        String username = getUsername(refreshToken);
        return new AuthenticationResponse(generateToken(username, jwtExpiration), generateToken(username, refreshExpiration));
    }

    public void validate(String token) {
        if (token == null) {
            throw new SecurityException();
        }

        try {
            Jwts.parser().verifyWith(key()).build().parse(token);
        } catch (JwtException e) {
            throw new JwtTokenExpiredException(e);
        }
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(key()).build().parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private String generateToken(UserDetails userDetails, long jwtExpiration) {
        return generateToken(userDetails.getUsername(), jwtExpiration);
    }

    private String generateToken(String username, long jwtExpiration) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key())
                .compact();
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(applicationKey));
    }

}
