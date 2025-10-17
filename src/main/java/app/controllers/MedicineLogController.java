package app.controllers;

import app.DAO.MedicineLogDAO;
import app.config.HibernateConfig;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

public class MedicineLogController {

    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private MedicineLogDAO medicineLogDAO = new MedicineLogDAO(emf);

    // Get all logs (evt. kun for én bruger)
    public void getAllLogs(Context ctx) {

    }

    // Get logs for a specific medicine
    public void getLogsByMedicine(Context ctx) {

    }

    // Create a log
    public void createLog(Context ctx) {

    }

    // Update a log (fx ændre dosis)
    public void updateLog(Context ctx) {

    }

    // Delete a log
    public void deleteLog(Context ctx) {

    }

    /// TODO: addMedicineLog -Tilføj log til den medicin

}
