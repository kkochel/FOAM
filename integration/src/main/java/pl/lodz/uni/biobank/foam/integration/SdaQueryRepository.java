package pl.lodz.uni.biobank.foam.integration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import pl.lodz.uni.biobank.foam.shared.FileData;

import java.nio.file.Paths;
import java.util.List;

@Repository
public class SdaQueryRepository {

    @PersistenceContext
    private EntityManager em;

    public DatasetData getDatasetWithFiles(String datasetId) {
        TypedQuery<Dataset> datasetQuery = em.createNamedQuery("getDatasetByStableId", Dataset.class);
        datasetQuery.setParameter("stableId", datasetId);
        Dataset dataset = datasetQuery.getSingleResult();

        TypedQuery<File> datasetFilesQuery = em.createNamedQuery("getFilesFromDataset", File.class);
        datasetFilesQuery.setParameter("datasetId", datasetId);
        List<File> resultList = datasetFilesQuery.getResultList();

        List<FileData> files = resultList.stream()
                .map(e -> new FileData(
                        e.getStableId(),
                        e.getSubmissionFilePath(),
                        Paths.get(e.getArchiveFilePath()).getFileName().toString(),
                        e.getArchiveFileSize(),
                        e.getDecryptedFileSize(),
                        e.getHeader()))
                .toList();

        return new DatasetData(dataset.getStableId(), dataset.getTitle(), dataset.getDescription(), files);
    }
}
