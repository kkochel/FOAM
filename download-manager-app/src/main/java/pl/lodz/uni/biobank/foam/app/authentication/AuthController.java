package pl.lodz.uni.biobank.foam.app.authentication;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private static final String CLEAR_SITE_DATA_HEADER = "Clear-Site-Data";

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("sign-in")
    public ResponseEntity<HttpStatus> signIn(@RequestBody SignInRequest request, HttpServletResponse response) {
        log.info("Handle signIn: " + request.username());
        AuthenticationResponse res = service.authenticate(request);
        response.addCookie(res.token());
        response.addCookie(res.refreshToken());

        log.info("SignIn response for: {} is: {}", request.username(), HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("refresh-token")
    public ResponseEntity<HttpStatus> refreshToken(@CookieValue("refreshToken") String refreshToken, HttpServletResponse response) {
        AuthenticationResponse res = service.refreshToken(refreshToken);
        response.addCookie(res.token());
        response.addCookie(res.refreshToken());

        log.info("Token refreshed");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("sign-out")
    public void sigOut(HttpServletResponse response) {
        response.addHeader(CLEAR_SITE_DATA_HEADER, "\"cache\", \"cookies\"");
        response.setStatus(HttpStatus.NO_CONTENT.value());
        SecurityContextHolder.clearContext();
    }

    @GetMapping("is-authenticated")
    public ResponseEntity<HttpStatus> isAuthenticated(@CookieValue(value = "token", required = false) String token) {
        return ResponseEntity.status(service.isAuthenticated(token)).build();
    }
}
