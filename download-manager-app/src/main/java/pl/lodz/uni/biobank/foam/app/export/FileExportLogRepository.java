package pl.lodz.uni.biobank.foam.app.export;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileExportLogRepository extends JpaRepository<FileExportLog, Long> {

    @Query("FROM FileExportLog fel WHERE fel.stableId = :fileId AND fel.username = :username")
    List<FileExportLog> getFileLogs(@Param("fileId") String fileId, @Param("username") String username);

}
