package app.DAO;

import Security.entities.User;
import app.config.HibernateConfig;
import app.entities.Medicine;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MedicineDAOTest {
    private static EntityManagerFactory emf;
    private static MedicineDAO dao;
    private User user;

    @BeforeAll
    static void setUp() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        dao = new MedicineDAO(emf);
    }

   /* @AfterAll
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

            // Opret en testbruger
            user = new User();
            user.setUsername("mariam");
            user.setPassword("test123");
            em.persist(user);

            // Opret nogle mediciner
            Medicine m1 = Medicine.builder()
                    .name("Panodil")
                    .type("painkiller")
                    .symptomDescription("smerter")
                    .user(user)
                    .build();

            Medicine m2 = Medicine.builder()
                    .name("Sertralin")
                    .type("antidepressant")
                    .symptomDescription("angst og depression")
                    .user(user)
                    .build();

            em.persist(m1);
            em.persist(m2);
            tx.commit();

        }catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }

    }

    @Test
    void create() {
        Medicine newMed = Medicine.builder()
                .name("Ibuprofen")
                .type("PainKiller")
                .symptomDescription("hovedpine")
                .user(user)
                .build();

        Medicine created = dao.create(newMed);
        assertNotNull(created.getId());
        assertEquals("Ibuprofen", created.getName());

    }

    @Test
    void getById() {
        Medicine medicine = dao.getByName("Panodil");
        Medicine found = dao.getById(medicine.getId());
        assertNotNull(found);
        assertEquals("Panodil", found.getName());
    }

    @Test
    void update() {
        Medicine medicine = dao.getByName("Sertralin");
        medicine.setSymptomDescription("opdateret beskrivelse");
        Medicine updated = dao.update(medicine);

        assertEquals("opdateret beskrivelse", updated.getSymptomDescription());

    }

    @Test
    void getAll() {
        List<Medicine> medicineList = dao.getAll();

        assertNotNull(medicineList);
        assertEquals(2, medicineList.size());
    }

    @Test
    void delete() {
        Medicine medicine = dao.getByName("Panodil");
        boolean deleted = dao.delete(medicine.getId());
        assertTrue(deleted);
        assertNull(dao.getById(medicine.getId()));
    }

    @Test
    void getByUsername() {
        List<Medicine> medicines = dao.getByUsername("mariam");
        assertEquals(2, medicines.size());
    }

    @Test
    void getByName() {
        Medicine medicine = dao.getByName("Sertralin");
        assertNotNull(medicine);
        assertEquals("Sertralin", medicine.getName());
    }
}