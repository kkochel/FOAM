package pl.lodz.uni.biobank.foam.integration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.uni.biobank.foam.shared.FileData;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
public class IntegrationServiceTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.2")
            .withDatabaseName("foam")
            .withUsername("biobank")
            .withPassword("biobank");

    @Container
    static RabbitMQContainer rabbitMQ = new RabbitMQContainer("rabbitmq:3-management-alpine")
            .withQueue("foam_integration");

    @MockBean
    private SdaQueryRepository sdaQueryRepository;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.rabbitmq.host", rabbitMQ::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQ::getAmqpPort);
        registry.add("spring.rabbitmq.username", rabbitMQ::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbitMQ::getAdminPassword);

        registry.add("app.foam-dataset.routing-key", () -> "foam_dataset");
    }

    @BeforeAll
    static void setup() {
        postgres.start();
        rabbitMQ.start();
    }

    @AfterAll
    static void tearDown() {
        postgres.stop();
        rabbitMQ.stop();
    }

    @Test
    void testDatasetReleaseHandlingWithMultipleFiles() {
        // Arrange
        String datasetId = "EGAD00001000001";
        DatasetRelease release = new DatasetRelease("dataset", datasetId);

        List<FileData> files = List.of(
                new FileData("file1", "path/to/file1", "file1.c4gh", 1000L, 900L, "header1"),
                new FileData("file2", "path/to/file2", "file2.c4gh", 2000L, 1800L, "header2")
        );

        DatasetData datasetData = new DatasetData(datasetId, "Test Dataset", "Test Description", files);

        org.mockito.Mockito.when(sdaQueryRepository.getDatasetWithFiles(datasetId))
                .thenReturn(datasetData);

        // Act
        String routingKey = "foam.dataset";
        SdaListener listener = new SdaListener(new DatasetReleaseService(sdaQueryRepository, new FoamDatasetSender(rabbitTemplate, routingKey)));
        listener.handleEvent(release);

        // Assert
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(rabbitTemplate, times(1)).convertAndSend(eq(routingKey), any(DatasetData.class));
        });
    }

    @Test
    void testDatasetReleaseHandlingWithSingleFile() {
        // Arrange
        String datasetId = "EGAD00001000002";
        DatasetRelease release = new DatasetRelease("dataset", datasetId);

        List<FileData> files = List.of(
                new FileData("file1", "path/to/file1", "file1.c4gh", 1000L, 900L, "header1")
        );

        DatasetData datasetData = new DatasetData(datasetId, "Test Dataset Single File", "Test Description for Single File", files);

        org.mockito.Mockito.when(sdaQueryRepository.getDatasetWithFiles(datasetId))
                .thenReturn(datasetData);

        // Act
        String routingKey = "foam.dataset";
        SdaListener listener = new SdaListener(new DatasetReleaseService(sdaQueryRepository, new FoamDatasetSender(rabbitTemplate, routingKey)));
        listener.handleEvent(release);

        // Assert
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(rabbitTemplate, times(1)).convertAndSend(eq(routingKey), eq(datasetData));
        });
    }

    @Test
    void testDatasetReleaseHandlingWithNoFiles() {
        // Arrange
        String datasetId = "EGAD00001000003";
        DatasetRelease release = new DatasetRelease("dataset", datasetId);

        DatasetData datasetData = new DatasetData(datasetId, "Test Dataset No Files", "Test Description for No Files", Collections.emptyList());

        org.mockito.Mockito.when(sdaQueryRepository.getDatasetWithFiles(datasetId))
                .thenReturn(datasetData);

        // Act
        String routingKey = "foam.dataset";
        SdaListener listener = new SdaListener(new DatasetReleaseService(sdaQueryRepository, new FoamDatasetSender(rabbitTemplate, routingKey)));
        listener.handleEvent(release);

        // Assert
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(rabbitTemplate, times(1)).convertAndSend(eq(routingKey), eq(datasetData));
        });
    }

    @Test
    void testDatasetReleaseHandlingWithNonExistentDataset() {
        // Arrange
        String datasetId = "EGAD00001000004";
        DatasetRelease release = new DatasetRelease("dataset", datasetId);

        org.mockito.Mockito.when(sdaQueryRepository.getDatasetWithFiles(datasetId))
                .thenThrow(new jakarta.persistence.NoResultException("Dataset not found"));

        // Act & Assert
        String routingKey = "foam.dataset";
        SdaListener listener = new SdaListener(new DatasetReleaseService(sdaQueryRepository, new FoamDatasetSender(rabbitTemplate, routingKey)));

        assertThrows(jakarta.persistence.NoResultException.class, () -> {
            listener.handleEvent(release);
        });

        verify(rabbitTemplate, never()).convertAndSend(any(String.class), any(DatasetData.class));
    }
}
