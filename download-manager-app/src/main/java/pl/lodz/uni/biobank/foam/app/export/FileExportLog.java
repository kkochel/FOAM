package pl.lodz.uni.biobank.foam.app.export;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Table(schema = "export", name = "export_log")
public class FileExportLog {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "export_log_generator")
    @SequenceGenerator(name = "export_log_generator", sequenceName = "export_log_seq", schema = "export", allocationSize = 1)
    private Long id;

    @Column(name = "stable_id")
    private String stableId;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "uuid")
    private UUID uuid;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "export_stage")
    private ExportStage exportStage;

    protected FileExportLog() {
    }

    private FileExportLog(String stableId, String username, UUID uuid) {
        this.stableId = stableId;
        this.username = username;
        this.uuid = uuid;
    }

    public FileExportLog(String stableId, String username, UUID uuid, ExportStage exportStage) {
        this.stableId = stableId;
        this.username = username;
        this.uuid = uuid;
        this.exportStage = exportStage;
    }

    public String getStableId() {
        return stableId;
    }

    public String getUsername() {
        return username;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public ExportStage getExportStage() {
        return exportStage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileExportLog that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(stableId, that.stableId) && Objects.equals(username, that.username) && Objects.equals(uuid, that.uuid) && Objects.equals(createdAt, that.createdAt) && exportStage == that.exportStage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stableId, username, uuid, createdAt, exportStage);
    }

}
