package pl.lodz.uni.biobank.foam.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pl.lodz.uni.biobank.foam.app.authentication.JwtFilter;
import pl.lodz.uni.biobank.foam.app.authentication.MyBasicAuthenticationEntryPoint;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Security configuration for the application.
 * Configures authentication, authorization, CSRF protection, CORS, and other security features.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
    // Endpoints that don't require authentication
    private static final String[] WHITE_LIST_URL = {
        "/api/auth/sign-in", 
        "/api/auth/refresh-token",
        "/api/auth/is-authenticated"
    };

    // Endpoints that are exempt from CSRF protection (typically for login)
    private static final String[] CSRF_EXEMPT_URL = {
        "/api/auth/sign-in"
    };

    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;
    private final MyBasicAuthenticationEntryPoint entryPoint;

    @Value("${application.allowed-origins}")
    private String[] allowedOrigins;

    public SecurityConfiguration(JwtFilter jwtFilter, AuthenticationProvider authenticationProvider, MyBasicAuthenticationEntryPoint entryPoint) {
        this.jwtFilter = jwtFilter;
        this.authenticationProvider = authenticationProvider;
        this.entryPoint = entryPoint;
    }

    /**
     * Configures the security filter chain.
     * 
     * @param http the HttpSecurity to modify
     * @return the built SecurityFilterChain
     * @throws Exception if an error occurs
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Configure request authorization
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(WHITE_LIST_URL).permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Allow preflight requests
                .anyRequest().authenticated()
            )
            // Configure stateless session management (appropriate for JWT)
            .sessionManagement(session -> session
                .sessionCreationPolicy(STATELESS)
            )
            // Configure authentication provider
            .authenticationProvider(authenticationProvider)
            // Add JWT filter before UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            // Configure CSRF protection
            .csrf(csrf -> csrf
                // Use cookie-based CSRF protection
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                // Use the default request handler
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                // Exempt specific endpoints from CSRF protection
                .ignoringRequestMatchers(CSRF_EXEMPT_URL)
            )
            // Configure CORS
            .cors(cors -> cors
                .configurationSource(corsConfigurationSource())
            )
            // Configure exception handling
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(entryPoint)
            )
            // Configure logout
            .logout(logout -> logout
                .logoutUrl("/api/auth/sign-out")
                .deleteCookies("token", "refreshToken", "XSRF-TOKEN")
                .clearAuthentication(true)
            );

        return http.build();
    }

    /**
     * Configures CORS settings.
     * 
     * @return the CORS configuration source
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Configure allowed origins
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));

        // Configure allowed methods
        configuration.setAllowedMethods(Arrays.asList(
            HttpMethod.GET.name(),
            HttpMethod.POST.name(),
            HttpMethod.OPTIONS.name()
        ));

        // Configure allowed headers
        configuration.setAllowedHeaders(List.of(
            "Authorization",
            "Content-Type",
            "X-Requested-With",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers",
            "X-XSRF-TOKEN"
        ));

        // Configure exposed headers
        configuration.setExposedHeaders(List.of(
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"
        ));

        // Allow credentials
        configuration.setAllowCredentials(true);

        // Configure max age (1 hour)
        configuration.setMaxAge(3600L);

        // Register the configuration for all paths
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
