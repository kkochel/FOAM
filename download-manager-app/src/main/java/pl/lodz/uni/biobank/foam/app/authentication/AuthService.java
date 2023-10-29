package pl.lodz.uni.biobank.foam.app.authentication;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager am;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.am = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthenticationResponse authenticate(SignInRequest request) {
        Authentication authenticate = am.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        return jwtService.generateTokens(authenticate);
    }

    public AuthenticationResponse refreshToken(RefreshTokenQuery refreshTokenQuery) {
        return jwtService.refreshToken(refreshTokenQuery.refreshToken());
    }
}
