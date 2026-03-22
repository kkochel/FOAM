package pl.lodz.uni.biobank.foam.integration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import pl.lodz.uni.biobank.foam.shared.FileData;
import pl.lodz.uni.biobank.foam.shared.I2B2Integration;

import java.nio.file.Paths;
import java.util.List;

/**
 * Repository for querying SDA (Sensitive Data Archive) datasets and files.
 * Provides methods to retrieve dataset information along with associated files,
 * and specialized queries for I2B2 integration files.
 */
@Repository
public class SdaQueryRepository {

    private static final String I2B2_PATH_PATTERN = "/i2b2/";
    private static final String QUERY_DATASET_BY_STABLE_ID = "getDatasetByStableId";
    private static final String QUERY_FILES_FROM_DATASET = "getFilesFromDataset";
    private static final String PARAM_STABLE_ID = "stableId";
    private static final String PARAM_DATASET_ID = "datasetId";

    @PersistenceContext
    private EntityManager em;

    /**
     * Retrieves a dataset with all its associated files.
     *
     * @param datasetId the stable ID of the dataset
     * @return DatasetData containing dataset metadata and list of files
     */
    public DatasetData getDatasetWithFiles(String datasetId) {
        Dataset dataset = findDatasetByStableId(datasetId);
        List<File> files = findFilesByDatasetId(datasetId);
        List<FileData> fileDataList = mapFilesToFileData(files);

        return new DatasetData(
                dataset.getStableId(),
                dataset.getTitle(),
                dataset.getDescription(),
                fileDataList
        );
    }

    /**
     * Retrieves I2B2 integration files for a specific dataset.
     * Only files with submission paths containing "/i2b2/" are included.
     *
     * @param datasetId the stable ID of the dataset
     * @return list of I2B2Integration records containing file paths and dataset ID
     */
    public List<I2B2Integration> getI2B2Files(String datasetId) {
        Dataset dataset = findDatasetByStableId(datasetId);
        List<File> files = findFilesByDatasetId(datasetId);

        return files.stream()
                .filter(this::isI2B2File)
                .map(file -> mapToI2B2Integration(file, dataset.getStableId()))
                .toList();
    }

    /**
     * Finds a dataset by its stable ID.
     *
     * @param datasetId the stable ID of the dataset
     * @return the Dataset entity
     */
    private Dataset findDatasetByStableId(String datasetId) {
        TypedQuery<Dataset> query = em.createNamedQuery(QUERY_DATASET_BY_STABLE_ID, Dataset.class);
        query.setParameter(PARAM_STABLE_ID, datasetId);
        return query.getSingleResult();
    }

    /**
     * Finds all files associated with a dataset.
     *
     * @param datasetId the stable ID of the dataset
     * @return list of File entities
     */
    private List<File> findFilesByDatasetId(String datasetId) {
        TypedQuery<File> query = em.createNamedQuery(QUERY_FILES_FROM_DATASET, File.class);
        query.setParameter(PARAM_DATASET_ID, datasetId);
        return query.getResultList();
    }

    /**
     * Checks if a file is an I2B2 file based on its submission path.
     *
     * @param file the File entity to check
     * @return true if the file path contains the I2B2 pattern (case-insensitive)
     */
    private boolean isI2B2File(File file) {
        String submissionPath = file.getSubmissionFilePath();
        return submissionPath != null &&
               submissionPath.toLowerCase().contains(I2B2_PATH_PATTERN);
    }

    /**
     * Maps a File entity to FileData DTO.
     *
     * @param file the File entity
     * @return FileData DTO
     */
    private FileData mapToFileData(File file) {
        return new FileData(
                file.getStableId(),
                file.getSubmissionFilePath(),
                extractFileName(file.getArchiveFilePath()),
                file.getArchiveFileSize(),
                file.getDecryptedFileSize(),
                file.getHeader()
        );
    }

    /**
     * Maps a list of File entities to FileData DTOs.
     *
     * @param files list of File entities
     * @return list of FileData DTOs
     */
    private List<FileData> mapFilesToFileData(List<File> files) {
        return files.stream()
                .map(this::mapToFileData)
                .toList();
    }

    /**
     * Maps a File entity to I2B2Integration record.
     *
     * @param file the File entity
     * @param datasetStableId the stable ID of the dataset
     * @return I2B2Integration record
     */
    private I2B2Integration mapToI2B2Integration(File file, String datasetStableId) {
        return new I2B2Integration(file.getSubmissionFilePath(), datasetStableId);
    }

    /**
     * Extracts the filename from a full file path.
     *
     * @param archiveFilePath the full path to the archive file
     * @return the filename only
     */
    private String extractFileName(String archiveFilePath) {
        return Paths.get(archiveFilePath).getFileName().toString();
    }
}
