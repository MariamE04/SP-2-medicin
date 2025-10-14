package app.DAO;

import app.entities.MedicineLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;

public class MedicineLogDAO implements IDAO<MedicineLog, Integer> {
    private final EntityManagerFactory emf;

    public MedicineLogDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public MedicineLog creat(MedicineLog log) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(log);
            em.getTransaction().commit();
            return log;
        }
    }

    @Override
    public MedicineLog getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(MedicineLog.class, id);
        }
    }

    @Override
    public MedicineLog update(MedicineLog log) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            MedicineLog updated = em.merge(log);
            em.getTransaction().commit();
            return updated;
        }
    }

    @Override
    public List<MedicineLog> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT l FROM MedicineLog l", MedicineLog.class)
                    .getResultList();
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            MedicineLog toDelete = em.find(MedicineLog.class, id);
            if (toDelete != null) {
                em.remove(toDelete);
                em.getTransaction().commit();
                return true;
            } else {
                em.getTransaction().rollback();
                return false;
            }
        }
    }
}
