package app.DAO;

import Security.entities.User;
import app.config.HibernateConfig;
import app.entities.Medicine;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;

import java.util.List;

public class MedicineDAO implements IDAO<Medicine, Integer> {
    private final EntityManagerFactory emf;

    public MedicineDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Medicine create(Medicine medicine) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(medicine);
            em.getTransaction().commit();
        }
        return medicine;
    }

    @Override
    public Medicine getById(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            return em.find(Medicine.class, id);
        }
    }

    @Override
    public Medicine update(Medicine medicine) {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Medicine updated = em.merge(medicine);
            em.getTransaction().commit();
            return updated;
        }
    }

    @Override
    public List<Medicine> getAll() {
        try(EntityManager em = emf.createEntityManager()){
            List<Medicine> medicines = em.createQuery("SELECT m FROM Medicine m", Medicine.class)
                    .getResultList();
            return medicines;
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Medicine toDelete = em.find(Medicine.class, id);

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
