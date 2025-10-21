package app;

import Security.daos.SecurityDAO;
import Security.entities.Role;
import Security.entities.User;
import app.DAO.MedicineDAO;
import app.DAO.MedicineLogDAO;
import app.entities.Medicine;
import app.entities.MedicineLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDateTime;

public class Populater {
    public static void populate(EntityManagerFactory emf) {
        // DAO’er
        SecurityDAO securityDAO = new SecurityDAO(emf);
        MedicineDAO medicineDAO = new MedicineDAO(emf);
        MedicineLogDAO logDAO = new MedicineLogDAO(emf);


        try {
            // Opreter roller
            Role userRole = securityDAO.createRole("USER");
            Role adminRole = securityDAO.createRole("ADMIN");

            // Opretter brugere
            User mariam = securityDAO.createUser("mariam", "1234");
            User ahmed = securityDAO.createUser("ahmed", "1234");
            User sofia = securityDAO.createUser("sofia", "1234");
            User lina = securityDAO.createUser("lina", "1234");
            User ali = securityDAO.createUser("ali", "1234");

            // Tilføjer roller til brugere
            securityDAO.addUserRole("mariam", "USER");
            securityDAO.addUserRole("ahmed", "USER");
            securityDAO.addUserRole("sofia", "ADMIN");
            securityDAO.addUserRole("lina", "USER");
            securityDAO.addUserRole("ali", "USER");

            // Opretter medicin til brugerne
            Medicine m1 = Medicine.builder()
                    .name("Sertralin")
                    .type("Antidepressant")
                    .symptomDescription("Behandling af angst og depression")
                    .user(mariam)
                    .build();

            Medicine m2 = Medicine.builder()
                    .name("Ibuprofen")
                    .type("Painkiller")
                    .symptomDescription("Behandling af smerte og feber")
                    .user(ahmed)
                    .build();

            Medicine m3 = Medicine.builder()
                    .name("Paracetamol")
                    .type("Painkiller")
                    .symptomDescription("Behandling af hovedpine")
                    .user(sofia)
                    .build();

            Medicine m4 = Medicine.builder()
                    .name("Vitamin D")
                    .type("Vitamin")
                    .symptomDescription("Tilskud ved mangel")
                    .user(lina)
                    .build();

            Medicine m5 = Medicine.builder()
                    .name("Metformin")
                    .type("Diabetes medicin")
                    .symptomDescription("Behandling af type 2 diabetes")
                    .user(ali)
                    .build();

            medicineDAO.create(m1);
            medicineDAO.create(m2);
            medicineDAO.create(m3);
            medicineDAO.create(m4);
            medicineDAO.create(m5);

            // Opretter medicine logs
            MedicineLog log1 = MedicineLog.builder()
                    .dose(25.0)
                    .takenAt(LocalDateTime.now().minusHours(2))
                    .medicine(m1)
                    .user(mariam)
                    .build();

            MedicineLog log2 = MedicineLog.builder()
                    .dose(400.0)
                    .takenAt(LocalDateTime.now().minusDays(1))
                    .medicine(m2)
                    .user(ahmed)
                    .build();

            MedicineLog log3 = MedicineLog.builder()
                    .dose(500.0)
                    .takenAt(LocalDateTime.now().minusDays(3))
                    .medicine(m3)
                    .user(sofia)
                    .build();

            MedicineLog log4 = MedicineLog.builder()
                    .dose(1000.0)
                    .takenAt(LocalDateTime.now().minusDays(5))
                    .medicine(m4)
                    .user(lina)
                    .build();

            MedicineLog log5 = MedicineLog.builder()
                    .dose(850.0)
                    .takenAt(LocalDateTime.now().minusHours(6))
                    .medicine(m5)
                    .user(ali)
                    .build();

            logDAO.create(log1);
            logDAO.create(log2);
            logDAO.create(log3);
            logDAO.create(log4);
            logDAO.create(log5);

            System.out.println("Database populated successfully with users, roles, medicine and logs!");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while populating database: " + e.getMessage());
        }
    }
}
