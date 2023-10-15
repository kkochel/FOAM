package pl.lodz.uni.biobank.foam.app.dataset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DatasetFileRepository extends JpaRepository<DatasetFile, Long> {

    @Query("FROM DatasetFile df WHERE df.stableId = :stableId")
    DatasetFile getFile(@Param("stableId") String stableId);
}
