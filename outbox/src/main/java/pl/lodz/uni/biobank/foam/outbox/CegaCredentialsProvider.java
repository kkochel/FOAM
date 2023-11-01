package pl.lodz.uni.biobank.foam.outbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;

@Component
public class CegaCredentialsProvider {
    private static final Logger log = LoggerFactory.getLogger(CegaCredentialsProvider.class);

    @Value("${application.cega.endpoint}")
    private String cegaEndpoint;

    @Value("${application.cega.credentials}")
    private String cegaCredentials;

    private final RestTemplate restTemplate;

    public CegaCredentialsProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("cega-credentials")
    public Credentials getCredentials(String username) {
        ResponseEntity<Credentials> response = getCredentialsResponseEntity(getUrl(username), getHeaders());
        HttpStatus statusCode = (HttpStatus) response.getStatusCode();
        if (!HttpStatus.OK.equals(statusCode)) {
            log.error("Bad response from CentralEGA: {}, {}", statusCode.value(), statusCode.getReasonPhrase());
            throw new RestClientException(String.format("Bad response from CentralEGA: %s, %s", statusCode.value(), statusCode.getReasonPhrase()));
        }

        log.info("Correctly returned permissions for the user {}", username);
        return response.getBody();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(cegaCredentials.getBytes()));
        return headers;
    }

    private URL getUrl(String username) {
        try {
            return new URL(String.format(cegaEndpoint, username));
        } catch (MalformedURLException e) {
            log.error("Error when creating URL for user: {}", username);
            throw new RuntimeException(e);
        }
    }

    private ResponseEntity<Credentials> getCredentialsResponseEntity(URL url, HttpHeaders headers) {
        try {
            return restTemplate.exchange(url.toURI(), HttpMethod.GET, new HttpEntity<>(headers), Credentials.class);
        } catch (URISyntaxException e) {
            log.error("Error during query execution: {}", url.getPath());
            throw new RuntimeException(e);
        }
    }

}