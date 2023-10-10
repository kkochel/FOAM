package pl.lodz.uni.biobank.foam.integration;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@NamedNativeQuery(
        name = "getDatasetByStableId",
        query = "SELECT * FROM sda.datasets d WHERE d.stable_id = :stableId",
        resultClass = Dataset.class)

@Entity
@Table(schema = "sda", name = "datasets")
public class Dataset {

    @Id
    private Integer id;

    @Column(name = "stable_id")
    private String stableId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    protected Dataset() {
    }

    public Integer getId() {
        return id;
    }

    public String getStableId() {
        return stableId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dataset dataset)) return false;
        return Objects.equals(id, dataset.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
