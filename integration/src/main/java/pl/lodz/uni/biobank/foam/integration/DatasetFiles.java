package pl.lodz.uni.biobank.foam.integration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(schema = "sda", name = "file_dataset")
public class DatasetFiles {

    @Id
    private Integer id;

    @Column(name = "file_id")
    private UUID fileId;

    @Column(name = "dataset_id")
    private Integer dataset;

    protected DatasetFiles() {
    }

    public Integer getId() {
        return id;
    }

    public UUID getFileId() {
        return fileId;
    }

    public Integer getDataset() {
        return dataset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DatasetFiles that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
