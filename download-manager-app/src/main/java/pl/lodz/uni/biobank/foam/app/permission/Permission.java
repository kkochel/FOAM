package pl.lodz.uni.biobank.foam.app.permission;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(schema = "permission", name = "permissions")
public class Permission {

    @Id
    @Column(name = "username")
    private String username;

    @Id
    @Column(name = "dataset_id")
    private String datasetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PermissionStatus status;


    @Column(name = "updated_at")
    private Date updatedAt;

    protected Permission() {
    }

    public Permission(String username, String datasetId) {
        this.username = username;
        this.datasetId = datasetId;
        this.status = PermissionStatus.AVAILABLE;
    }

    public String getUsername() {
        return username;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public Permission revoke() {
        status = PermissionStatus.REVOKED;
        return this;
    }

    public Permission makeAvailable() {
         status = PermissionStatus.AVAILABLE;
         return this;
    }

    public PermissionStatus getStatus() {
        return status;
    }

    @PrePersist
    void saveUpdateTile(){
        this.updatedAt = new Date(System.currentTimeMillis());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission that)) return false;
        return Objects.equals(username, that.username) && Objects.equals(datasetId, that.datasetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, datasetId);
    }
}
