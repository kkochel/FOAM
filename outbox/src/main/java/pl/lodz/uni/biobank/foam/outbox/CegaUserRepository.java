package pl.lodz.uni.biobank.foam.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CegaUserRepository extends JpaRepository<CegaUser, Long> {

    @Query("SELECT u FROM CegaUser u WHERE u.username = :username")
    Optional<CegaUser> findByUsername(@Param("username") String username);
}
