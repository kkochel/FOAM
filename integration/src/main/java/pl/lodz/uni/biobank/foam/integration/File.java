package pl.lodz.uni.biobank.foam.integration;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@NamedNativeQuery(
        name = "getFilesFromDataset",
        query = "SELECT * FROM  sda.files as f  WHERE f.id IN (SELECT fi.id FROM sda.files fi LEFT JOIN sda.file_dataset fd on fi.id = fd.file_id LEFT JOIN sda.datasets d on d.id = fd.dataset_id WHERE d.stable_id = :datasetId)",
        resultClass = File.class)

@Entity
@Table(schema = "sda", name = "files")
public class File {

    @Id
    private UUID id;

    @Column(name = "stable_id")
    private String stableId;

    @Column(name = "submission_user")
    private String submissionUser;

    @Column(name = "submission_file_path")
    private String submissionFilePath;

    @Column(name = "submission_file_size")
    private Long submissionFileSize;

    @Column(name = "archive_file_path")
    private String archiveFilePath;

    @Column(name = "archive_file_size")
    private Long archiveFileSize;

    @Column(name = "decrypted_file_size")
    private Long decryptedFileSize;

    @Column(name = "backup_path")
    private String backupPath;

    @Column(name = "header")
    private String header;

    @Column(name = "encryption_method")
    private String encryptionMethod;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    protected File() {
    }

    public UUID getId() {
        return id;
    }

    public String getStableId() {
        return stableId;
    }

    public String getSubmissionUser() {
        return submissionUser;
    }

    public String getSubmissionFilePath() {
        return submissionFilePath;
    }

    public Long getSubmissionFileSize() {
        return submissionFileSize;
    }

    public String getArchiveFilePath() {
        return archiveFilePath;
    }

    public Long getArchiveFileSize() {
        return archiveFileSize;
    }

    public Long getDecryptedFileSize() {
        return decryptedFileSize;
    }

    public String getBackupPath() {
        return backupPath;
    }

    public String getHeader() {
        return header;
    }

    public String getEncryptionMethod() {
        return encryptionMethod;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof File file)) return false;
        return Objects.equals(id, file.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
