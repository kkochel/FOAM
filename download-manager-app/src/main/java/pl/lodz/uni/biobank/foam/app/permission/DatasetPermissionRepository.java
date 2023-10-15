package pl.lodz.uni.biobank.foam.app.permission;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class DatasetPermissionRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Permission> getPermission(String username, String datasetId) {
        TypedQuery<Permission> permissionQuery = em.createQuery("FROM Permission p WHERE p.username = :username AND p.datasetId = :datasetId", Permission.class);
        permissionQuery.setParameter("username", username);
        permissionQuery.setParameter("datasetId", datasetId);

        return permissionQuery.getResultList();
    }

    public List<String> getDatasetsId(String username) {
        TypedQuery<String> datasetIdQuery = em.createQuery("SELECT p.datasetId FROM Permission p WHERE p.username = :username", String.class);
        datasetIdQuery.setParameter("username", username);

        return datasetIdQuery.getResultList();
    }

    @Transactional
    public void persist(Permission permission) {
        em.persist(permission);
    }

    @Transactional
    public void merge(Permission permission) {
        em.merge(permission);
    }
}
