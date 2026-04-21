package pl.lodz.uni.biobank.foam.integration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pl.lodz.uni.biobank.foam.shared.I2B2Integration;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SdaQueryRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Dataset> datasetQuery;

    @Mock
    private TypedQuery<File> fileQuery;

    @InjectMocks
    private SdaQueryRepository repository;

    private Dataset testDataset;
    private List<File> testFiles;

    @BeforeEach
    void setUp() {
        testDataset = mock(Dataset.class);
        when(testDataset.getStableId()).thenReturn("DATASET-001");
        when(testDataset.getTitle()).thenReturn("Test Dataset");
        when(testDataset.getDescription()).thenReturn("Test Description");

        File i2b2File1 = mock(File.class);
        when(i2b2File1.getSubmissionFilePath()).thenReturn("/submissions/i2b2/patient_data.csv");
        when(i2b2File1.getStableId()).thenReturn("FILE-001");
        when(i2b2File1.getArchiveFilePath()).thenReturn("/archive/file1.c4gh");
        when(i2b2File1.getArchiveFileSize()).thenReturn(1024L);
        when(i2b2File1.getDecryptedFileSize()).thenReturn(900L);
        when(i2b2File1.getHeader()).thenReturn(Arrays.toString("header1".getBytes()));

        File i2b2File2 = mock(File.class);
        when(i2b2File2.getSubmissionFilePath()).thenReturn("/submissions/I2B2/observation_data.csv");
        when(i2b2File2.getStableId()).thenReturn("FILE-002");
        when(i2b2File2.getArchiveFilePath()).thenReturn("/archive/file2.c4gh");
        when(i2b2File2.getArchiveFileSize()).thenReturn(2048L);
        when(i2b2File2.getDecryptedFileSize()).thenReturn(1800L);
        when(i2b2File2.getHeader()).thenReturn(Arrays.toString("header2".getBytes()));

        File regularFile = mock(File.class);
        when(regularFile.getSubmissionFilePath()).thenReturn("/submissions/regular/data.csv");
        when(regularFile.getStableId()).thenReturn("FILE-003");
        when(regularFile.getArchiveFilePath()).thenReturn("/archive/file3.c4gh");
        when(regularFile.getArchiveFileSize()).thenReturn(512L);
        when(regularFile.getDecryptedFileSize()).thenReturn(450L);
        when(regularFile.getHeader()).thenReturn(Arrays.toString("header3".getBytes()));

        testFiles = List.of(i2b2File1, i2b2File2, regularFile);
    }

    @Test
    void getI2B2FilesShouldReturnOnlyI2B2Files() {
        // given
        String datasetId = "DATASET-001";
        when(entityManager.createNamedQuery("getDatasetByStableId", Dataset.class)).thenReturn(datasetQuery);
        when(datasetQuery.setParameter(anyString(), anyString())).thenReturn(datasetQuery);
        when(datasetQuery.getSingleResult()).thenReturn(testDataset);

        when(entityManager.createNamedQuery("getFilesFromDataset", File.class)).thenReturn(fileQuery);
        when(fileQuery.setParameter(anyString(), anyString())).thenReturn(fileQuery);
        when(fileQuery.getResultList()).thenReturn(testFiles);

        // when
        List<I2B2Integration> result = repository.getI2B2Files(datasetId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).submissionFilePath()).isEqualTo("/submissions/i2b2/patient_data.csv");
        assertThat(result.get(0).stableId()).isEqualTo("DATASET-001");
        assertThat(result.get(1).submissionFilePath()).isEqualTo("/submissions/I2B2/observation_data.csv");
        assertThat(result.get(1).stableId()).isEqualTo("DATASET-001");
    }

    @Test
    void getI2B2FilesShouldReturnEmptyListWhenNoI2B2Files() {
        // given
        String datasetId = "DATASET-002";
        File regularFile = mock(File.class);
        when(regularFile.getSubmissionFilePath()).thenReturn("/submissions/regular/data.csv");

        when(entityManager.createNamedQuery("getDatasetByStableId", Dataset.class)).thenReturn(datasetQuery);
        when(datasetQuery.setParameter(anyString(), anyString())).thenReturn(datasetQuery);
        when(datasetQuery.getSingleResult()).thenReturn(testDataset);

        when(entityManager.createNamedQuery("getFilesFromDataset", File.class)).thenReturn(fileQuery);
        when(fileQuery.setParameter(anyString(), anyString())).thenReturn(fileQuery);
        when(fileQuery.getResultList()).thenReturn(List.of(regularFile));

        // when
        List<I2B2Integration> result = repository.getI2B2Files(datasetId);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void getI2B2FilesShouldFilterCaseInsensitive() {
        // given
        String datasetId = "DATASET-003";
        File lowerCaseI2b2File = mock(File.class);
        when(lowerCaseI2b2File.getSubmissionFilePath()).thenReturn("/submissions/i2b2/data.csv");

        File upperCaseI2b2File = mock(File.class);
        when(upperCaseI2b2File.getSubmissionFilePath()).thenReturn("/submissions/I2B2/data.csv");

        File mixedCaseI2b2File = mock(File.class);
        when(mixedCaseI2b2File.getSubmissionFilePath()).thenReturn("/submissions/I2b2/data.csv");

        when(entityManager.createNamedQuery("getDatasetByStableId", Dataset.class)).thenReturn(datasetQuery);
        when(datasetQuery.setParameter(anyString(), anyString())).thenReturn(datasetQuery);
        when(datasetQuery.getSingleResult()).thenReturn(testDataset);

        when(entityManager.createNamedQuery("getFilesFromDataset", File.class)).thenReturn(fileQuery);
        when(fileQuery.setParameter(anyString(), anyString())).thenReturn(fileQuery);
        when(fileQuery.getResultList()).thenReturn(List.of(lowerCaseI2b2File, upperCaseI2b2File, mixedCaseI2b2File));

        // when
        List<I2B2Integration> result = repository.getI2B2Files(datasetId);

        // then
        assertThat(result).hasSize(3);
    }

    @Test
    void getI2B2FilesShouldCallEntityManagerWithCorrectParameters() {
        // given
        String datasetId = "DATASET-001";
        when(entityManager.createNamedQuery("getDatasetByStableId", Dataset.class)).thenReturn(datasetQuery);
        when(datasetQuery.setParameter(anyString(), anyString())).thenReturn(datasetQuery);
        when(datasetQuery.getSingleResult()).thenReturn(testDataset);

        when(entityManager.createNamedQuery("getFilesFromDataset", File.class)).thenReturn(fileQuery);
        when(fileQuery.setParameter(anyString(), anyString())).thenReturn(fileQuery);
        when(fileQuery.getResultList()).thenReturn(testFiles);

        // when
        repository.getI2B2Files(datasetId);

        // then
        verify(entityManager).createNamedQuery("getDatasetByStableId", Dataset.class);
        verify(datasetQuery).setParameter("stableId", datasetId);
        verify(entityManager).createNamedQuery("getFilesFromDataset", File.class);
        verify(fileQuery).setParameter("datasetId", datasetId);
    }
}
