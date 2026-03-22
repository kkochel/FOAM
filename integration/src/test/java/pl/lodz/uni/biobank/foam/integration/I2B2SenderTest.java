package pl.lodz.uni.biobank.foam.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class I2B2SenderTest {

    @Mock
    private RestTemplate restTemplate;

    private I2B2Sender i2b2Sender;
    private final String apiUrl = "http://test-api-url:8001/api/fega_connect_i2b2";

    @BeforeEach
    void setUp() {
        i2b2Sender = new I2B2Sender(restTemplate);
        ReflectionTestUtils.setField(i2b2Sender, "apiUrl", apiUrl);
    }

    @Test
    void handleSendShouldSendDataToApiWithCorrectUrl() {
        // given
        List<pl.lodz.uni.biobank.foam.shared.I2B2Integration> data = List.of(
                new pl.lodz.uni.biobank.foam.shared.I2B2Integration("/path/to/i2b2/file1.txt", "dataset-001"),
                new pl.lodz.uni.biobank.foam.shared.I2B2Integration("/path/to/i2b2/file2.txt", "dataset-001")
        );
        when(restTemplate.postForEntity(eq(apiUrl), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok("success"));

        // when
        i2b2Sender.handleSend(data);

        // then
        verify(restTemplate, times(1)).postForEntity(eq(apiUrl), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void handleSendShouldSendEmptyListWhenNoData() {
        // given
        List<pl.lodz.uni.biobank.foam.shared.I2B2Integration> emptyData = List.of();
        when(restTemplate.postForEntity(eq(apiUrl), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok("success"));

        // when
        i2b2Sender.handleSend(emptyData);

        // then
        verify(restTemplate, times(1)).postForEntity(eq(apiUrl), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void handleSendShouldPassExactDataAsCsv() {
        // given
        List<pl.lodz.uni.biobank.foam.shared.I2B2Integration> data = List.of(
                new pl.lodz.uni.biobank.foam.shared.I2B2Integration("/submissions/i2b2/patient_data.csv", "STUDY-2024-001")
        );
        ArgumentCaptor<HttpEntity<MultiValueMap<String, Object>>> captor = ArgumentCaptor.forClass(HttpEntity.class);
        when(restTemplate.postForEntity(eq(apiUrl), captor.capture(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("success"));

        // when
        i2b2Sender.handleSend(data);

        // then
        verify(restTemplate).postForEntity(eq(apiUrl), any(HttpEntity.class), eq(String.class));
        HttpEntity<MultiValueMap<String, Object>> capturedRequest = captor.getValue();
        MultiValueMap<String, Object> body = capturedRequest.getBody();

        assertThat(body).isNotNull();
        assertThat(body.containsKey("file")).isTrue();

        ByteArrayResource fileResource = (ByteArrayResource) body.getFirst("file");
        String csvContent = new String(fileResource.getByteArray(), StandardCharsets.UTF_8);

        assertThat(csvContent).contains("i2b2_ID,fega_ID");
        assertThat(csvContent).contains("patient_data,STUDY-2024-001");
    }

    @Test
    void handleSendShouldExtractFilenameWithoutExtension() {
        // given
        List<pl.lodz.uni.biobank.foam.shared.I2B2Integration> data = List.of(
                new pl.lodz.uni.biobank.foam.shared.I2B2Integration("/path/to/file.txt", "dataset-001"),
                new pl.lodz.uni.biobank.foam.shared.I2B2Integration("C:\\Windows\\path\\document.pdf", "dataset-002"),
                new pl.lodz.uni.biobank.foam.shared.I2B2Integration("simple.csv", "dataset-003"),
                new pl.lodz.uni.biobank.foam.shared.I2B2Integration("/no/extension/file", "dataset-004")
        );
        ArgumentCaptor<HttpEntity<MultiValueMap<String, Object>>> captor = ArgumentCaptor.forClass(HttpEntity.class);
        when(restTemplate.postForEntity(eq(apiUrl), captor.capture(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("success"));

        // when
        i2b2Sender.handleSend(data);

        // then
        ByteArrayResource fileResource = (ByteArrayResource) captor.getValue().getBody().getFirst("file");
        String csvContent = new String(fileResource.getByteArray(), StandardCharsets.UTF_8);

        assertThat(csvContent).contains("file,dataset-001");
        assertThat(csvContent).contains("document,dataset-002");
        assertThat(csvContent).contains("simple,dataset-003");
        assertThat(csvContent).contains("file,dataset-004");
    }
}
