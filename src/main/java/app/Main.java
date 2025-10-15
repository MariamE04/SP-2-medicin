package app;

import app.DAO.MedicineDAO;
import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.entities.Medicine;
import app.entities.MedicineLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDateTime;

public class  Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        System.out.println("test");

        ApplicationConfig.startServer(7007);

        MedicineDAO medicineDAO = new MedicineDAO(emf);

        // 1️⃣ Opret en medicin
        Medicine m1 = Medicine.builder()
                .name("Sertralin")
                .type("Antidepressant")
                .symptomDescription("Angst og depression")
                .build();

        // Gem i DB
        medicineDAO.creat(m1);
        System.out.println("Gemte medicin: " + m1.getName());

        // 2️⃣ Hent medicinen igen
        Medicine found = medicineDAO.getById(m1.getId());
        System.out.println("Hentet fra DB: " + found.getName());

        // 3️⃣ Opret et log for denne medicin
        MedicineLog log = new MedicineLog();
        log.setMedicine(found);
        log.setDose(25.0);
        log.setTakenAt(LocalDateTime.now());

        // Gem log direkte via EntityManager
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(log);
            em.getTransaction().commit();
            System.out.println("Gemte MedicineLog for " + found.getName());
        }

        // 4️⃣ Hent og print alle MedicineLog fra DB
        try (EntityManager em = emf.createEntityManager()) {
            var logs = em.createQuery("SELECT l FROM MedicineLog l", MedicineLog.class).getResultList();
            for (MedicineLog l : logs) {
                System.out.println("Log ID: " + l.getId() + " | Dose: " + l.getDose() + " | For medicin: " + l.getMedicine().getName());
            }
        }
    }
}

