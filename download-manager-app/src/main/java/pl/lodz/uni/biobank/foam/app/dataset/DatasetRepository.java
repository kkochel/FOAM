package pl.lodz.uni.biobank.foam.app.dataset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface DatasetRepository extends JpaRepository<Dataset, Long> {

    @Query("FROM Dataset d LEFT JOIN FETCH d.datasetFiles WHERE d.stableId = :stableId")
    Dataset getWithFilesBy(@Param("stableId") String stableId);
}
