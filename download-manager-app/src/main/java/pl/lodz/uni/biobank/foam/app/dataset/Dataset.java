package pl.lodz.uni.biobank.foam.app.dataset;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Table(schema = "dataset", name = "datasets")
public class Dataset {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "dataset_generator")
    @SequenceGenerator(name = "dataset_generator", sequenceName = "dataset_seq", schema = "dataset", allocationSize = 1)
    private Long id;

    @Column(name = "stable_id")
    private String stableId;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "dataset", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private final Set<DatasetFile> datasetFiles = new HashSet<>();


    protected Dataset() {
    }

    public Dataset(DatasetData dataset) {
        this.stableId = dataset.stableId();
        this.title = dataset.title();
        this.description = dataset.description();
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<DatasetFile> getFiles() {
        return datasetFiles;
    }

    public void addFile(DatasetFile datasetFile) {
        this.datasetFiles.add(datasetFile);
        datasetFile.setDataset(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dataset dataset)) return false;
        return Objects.equals(stableId, dataset.stableId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stableId);
    }
}
