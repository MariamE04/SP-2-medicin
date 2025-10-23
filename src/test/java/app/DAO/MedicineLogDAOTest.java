package app.DAO;

import Security.entities.User;
import app.config.HibernateConfig;
import app.entities.Medicine;
import app.entities.MedicineLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MedicineLogDAOTest {
  private static EntityManagerFactory emf;
    private static MedicineDAO medicineDAO;
    private static MedicineLogDAO medicineLogDAO;
    private User user;
    private Medicine med;
    private MedicineLog log1;
    private MedicineLog log2;


    @BeforeAll
    static void setUp() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        medicineLogDAO = new MedicineLogDAO(emf);
        medicineDAO = new MedicineDAO(emf);

    }

 /*   @AfterAll
    static void tearDown() {
        if (emf != null) {
            emf.close();
        }
    } */

    @BeforeEach
    void setUpTestData() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.createQuery("DELETE FROM MedicineLog").executeUpdate();
            em.createQuery("DELETE FROM Medicine").executeUpdate();
            em.createQuery("DELETE FROM User").executeUpdate();


            user = User.builder()
                    .username("testuser")
                    .password("secret123")
                    .build();
            em.persist(user);

          med = Medicine.builder()
                    .name("Ibuprofen")
                    .type("Painkiller")
                    .symptomDescription("Used for headaches and muscle pain")
                    .user(user)
                    .build();
            em.persist(med);

            log1 = MedicineLog.builder()
                    .dose(200.0)
                    .takenAt(LocalDateTime.now())
                    .user(user)
                    .medicine(med)
                    .build();
            em.persist(log1);

            log2 = MedicineLog.builder()
                    .dose(100.0)
                    .takenAt(LocalDateTime.now())
                    .user(user)
                    .medicine(med)
                    .build();
            em.persist(log2);

            tx.commit();

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Test
    void create() {
        MedicineLog log1 = MedicineLog.builder()
                .dose(200.0)
                .takenAt(LocalDateTime.now())
                .user(user)
                .medicine(med)
                .build();

        MedicineLog created = medicineLogDAO.create(log1);

        assertNotNull(created);
        assertTrue(created.getId() > 0);
        assertEquals(200.0, created.getDose());
        assertEquals(user.getUsername(), created.getUser().getUsername());
    }

    @Test
    void getById() {
        MedicineLog found = medicineLogDAO.getById(log1.getId());

        assertNotNull(found);
        assertEquals(200.0, found.getDose());
        assertEquals("Ibuprofen", found.getMedicine().getName());
        assertEquals("testuser", found.getUser().getUsername());
    }

    @Test
    void update() {
        MedicineLog medicineLog = medicineLogDAO.getById(log1.getId());
        medicineLog.setDose(100.0);
        MedicineLog updated = medicineLogDAO.update(medicineLog);

        assertEquals(100.0, updated.getDose());
    }

    @Test
    void getAll() {
        List<MedicineLog> medicineLogs = medicineLogDAO.getAll();
        assertEquals(2,medicineLogs.size());
    }

    @Test
    void delete() {
        MedicineLog medicineLog = medicineLogDAO.getById(log1.getId());
        boolean deleted = medicineLogDAO.delete(medicineLog.getId());
        assertTrue(deleted);
        assertNull(medicineLogDAO.getById(medicineLog.getId()));
    }

    @Test
    void getByUsername() {
        List<MedicineLog> logs = medicineLogDAO.getByUsername("testuser");
        assertEquals(2, logs.size());

    }
}