package app;

import app.DAO.MedicineDAO;
import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.entities.Medicine;
import app.entities.MedicineLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDateTime;

import static app.Populater.populate;

public class  Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        System.out.println("test");

        ApplicationConfig.startServer(7007);

        populate(HibernateConfig.getEntityManagerFactory());
    }
}

