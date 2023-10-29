package pl.lodz.uni.biobank.foam.app.export;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserExportFileRepository extends JpaRepository<UserFile, Long> {


    @Query("FROM UserFile uf WHERE uf.datasetId = :datasetId AND uf.username = :username")
    List<UserFile> getFiles(@Param("datasetId") String datasetId, @Param("username") String username);

    @Query("FROM UserFile uf WHERE uf.stableId = :fileId AND uf.username = :username")
    UserFile getFile(@Param("fileId") String fileId, @Param("username") String username);
}
