package pl.lodz.uni.biobank.foam.app.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class MyBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
            Optional<Cookie> refreshTokenCookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals("refreshToken")).findAny();
            Optional<Cookie> tokenCookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals("token")).findAny();

            if (tokenCookie.isEmpty() && refreshTokenCookie.isPresent()) {
                response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            }
    }
}
