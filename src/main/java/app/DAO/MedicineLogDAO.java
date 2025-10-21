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
    public MedicineLog create(MedicineLog log) {
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
            MedicineLog log = em.find(MedicineLog.class, id);

            if (log != null) {
                // fjern log fra Medicine
                if (log.getMedicine() != null) {
                    log.getMedicine().getLogs().remove(log);
                }

                // fjern log fra User
                if (log.getUser() != null) {
                    log.getUser().getLogs().remove(log);
                }

                // nu kan vi trygt slette loggen
                em.remove(log);
                em.getTransaction().commit();
                return true;
            } else {
                em.getTransaction().rollback();
                return false;
            }
        }
    }

    public List<MedicineLog> getByUsername(String username) {
        try (var em = emf.createEntityManager()) {
            return em.createQuery("SELECT l FROM MedicineLog l WHERE l.user.username = :username", MedicineLog.class)
                    .setParameter("username", username)
                    .getResultList();
        }
    }

}
