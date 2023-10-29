package pl.lodz.uni.biobank.foam.app.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("sign-in")
    public ResponseEntity<AuthenticationResponse> signIn(@RequestBody SignInRequest request) {
        log.info("Handle signIn: " + request.username());
        AuthenticationResponse response = service.authenticate(request);

        log.info("SignIn response for: {} is: {}", request.username(), HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    @PostMapping("refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenQuery refreshTokenQuery) {
        AuthenticationResponse response = service.refreshToken(refreshTokenQuery);

        log.info("Token refreshed");
        return ResponseEntity.ok(response);
    }

}
