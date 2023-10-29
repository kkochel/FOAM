package pl.lodz.uni.biobank.foam.app.export;

import jakarta.persistence.*;

import java.util.Objects;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Table(schema = "export", name = "users_files")
public class UserFile {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "users_files_generator")
    @SequenceGenerator(name = "users_files_generator", sequenceName = "users_files_seq", schema = "export", allocationSize = 1)
    private Long id;

    @Column(name = "dataset_id")
    private String datasetId;

    @Column(name = "stable_id")
    private String stableId;

    @Column(name = "username", unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "export_stage")
    private ExportStage exportStage;

    protected UserFile() {
    }

    public UserFile(String datasetId, String stableId, String username) {
        this.datasetId = datasetId;
        this.stableId = stableId;
        this.username = username;
    }

    public String getStableId() {
        return stableId;
    }

    public String getUsername() {
        return username;
    }

    public ExportStage getExportStage() {
        return exportStage;
    }

    public void updateStage(ExportStage exportStage) {
        this.exportStage = exportStage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserFile userFile)) return false;
        return Objects.equals(datasetId, userFile.datasetId) && Objects.equals(stableId, userFile.stableId) && Objects.equals(username, userFile.username) && exportStage == userFile.exportStage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(datasetId, stableId, username, exportStage);
    }
}
