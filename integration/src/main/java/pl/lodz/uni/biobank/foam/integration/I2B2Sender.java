package pl.lodz.uni.biobank.foam.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pl.lodz.uni.biobank.foam.shared.I2B2Integration;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class I2B2Sender {

    private static final Logger log = LoggerFactory.getLogger(I2B2Sender.class);

    @Value("${app.foam-dataset.i2b2-api-url}")
    private String apiUrl;
    private final RestTemplate restTemplate;

    public I2B2Sender(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void handleSend(List<I2B2Integration> data) {
        log.info("Starting I2B2 data send process. Number of records: {}", data.size());
        try {
            String csvContent = convertToCsv(data);
            sendCsvFile(csvContent);
            log.info("I2B2 data send process completed successfully");
        } catch (Exception e) {
            log.error("I2B2 data send process failed. Error: {}", e.getMessage(), e);
        }
    }

    private String convertToCsv(List<I2B2Integration> data) {
        log.debug("Converting {} I2B2Integration records to CSV format", data.size());
        StringBuilder csv = new StringBuilder();
        csv.append("i2b2_ID,fega_ID\n");

        for (I2B2Integration integration : data) {
            String filenameWithoutExtension = extractFilenameWithoutExtension(integration.submissionFilePath());
            csv.append(filenameWithoutExtension)
               .append(",")
               .append(integration.stableId())
               .append("\n");
        }

        log.debug("CSV conversion completed. CSV size: {} bytes", csv.length());
        return csv.toString();
    }

    private String extractFilenameWithoutExtension(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return "";
        }

        // Extract filename from path
        String filename = filePath.substring(filePath.lastIndexOf('/') + 1);
        filename = filename.substring(filename.lastIndexOf('\\') + 1);

        // Remove extension
        int lastDotIndex = filename.indexOf('.');
        if (lastDotIndex > 0) {
            filename = filename.substring(0, lastDotIndex);
        }

        if (!filename.matches("\\d+")) {
            throw new IllegalArgumentException(
                    "I2B2 ID must be numeric, but got: '" + filename + "' from path: " + filePath);
        }

        return filename;
    }

    private void sendCsvFile(String csvContent) {
        log.info("Sending CSV file to I2B2 API. URL: {}", apiUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ByteArrayResource fileResource = new ByteArrayResource(csvContent.getBytes(StandardCharsets.UTF_8)) {
            @Override
            public String getFilename() {
                return "data.csv";
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);
            log.info("Successfully sent CSV to I2B2 API. Response status: {}", response.getStatusCode());
            log.debug("Response body: {}", response.getBody());
        } catch (RestClientException e) {
            log.error("Failed to send CSV file to I2B2 API at URL: {}. Error: {}", apiUrl, e.getMessage(), e);
        }
    }
}
