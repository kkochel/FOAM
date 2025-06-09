package pl.lodz.uni.biobank.foam.app.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticationConfiguration {
    @Value("${application.jwt.secret-key}")
    private String applicationKey;
    @Value("${application.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.jwt.refresh-token.expiration}")
    private long refreshExpiration;
    @Value("${application.jwt.issuer:foam-application}")
    private String issuer;
    @Value("${application.jwt.audience:foam-users}")
    private String audience;


    @Bean
    public JwtService getJwtService() {
        return new JwtService(applicationKey, jwtExpiration, refreshExpiration, issuer, audience);
    }

}
