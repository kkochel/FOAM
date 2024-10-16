package pl.lodz.uni.biobank.foam.app.dataset;

import jakarta.persistence.*;
import pl.lodz.uni.biobank.foam.shared.FileData;

import java.util.Objects;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Table(schema = "dataset", name = "files")
public class DatasetFile {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "file_dataset_generator")
    @SequenceGenerator(name = "file_dataset_generator", sequenceName = "file_dataset_seq", schema = "dataset", allocationSize = 1)
    private Long id;

    @Column(name = "stable_id")
    private String stableId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "archive_file_path")
    private String archiveFilePath;

    @Column(name = "archive_file_size")
    private Long archiveFileSize;

    @Column(name = "decrypted_file_size")
    private Long decryptedFileSize;

    @Column(name = "header")
    private String header;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_dataset")
    private Dataset dataset;

    protected DatasetFile() {
    }

    public DatasetFile(FileData fd) {
        this.stableId = fd.stableId();
        this.fileName = fd.fileName();
        this.archiveFilePath = fd.archiveFilePath();
        this.archiveFileSize = fd.archiveFileSize();
        this.decryptedFileSize = fd.decryptedFileSize();
        this.header = fd.header();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStableId() {
        return stableId;
    }

    public void setStableId(String stableId) {
        this.stableId = stableId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getArchiveFilePath() {
        return archiveFilePath;
    }

    public void setArchiveFilePath(String archiveFilePath) {
        this.archiveFilePath = archiveFilePath;
    }

    public Long getArchiveFileSize() {
        return archiveFileSize;
    }

    public void setArchiveFileSize(Long archiveFileSize) {
        this.archiveFileSize = archiveFileSize;
    }

    public Long getDecryptedFileSize() {
        return decryptedFileSize;
    }

    public void setDecryptedFileSize(Long decryptedFileSize) {
        this.decryptedFileSize = decryptedFileSize;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DatasetFile datasetFile)) return false;
        return Objects.equals(stableId, datasetFile.stableId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stableId);
    }
}
