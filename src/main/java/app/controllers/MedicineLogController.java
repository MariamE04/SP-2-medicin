package app.controllers;

import Security.daos.SecurityDAO;
import app.DAO.MedicineDAO;
import app.DAO.MedicineLogDAO;
import app.config.HibernateConfig;
import app.dtos.MedicineLogDTO;
import app.entities.Medicine;
import app.entities.MedicineLog;
import app.mappers.MedicineLogMapper;
import dk.bugelhartmann.UserDTO;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class MedicineLogController {

    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private MedicineLogDAO medicineLogDAO = new MedicineLogDAO(emf);
    private MedicineDAO medicineDAO = new MedicineDAO(emf);
    private SecurityDAO securityDAO = new SecurityDAO(emf);

    // Get all logs (evt. kun for én bruger)
    public void getAllLogs(Context ctx) {
        List<MedicineLog> medicineLogList = medicineLogDAO.getAll();
        List<MedicineLogDTO> medicineLogDTOS = medicineLogList.stream().map(MedicineLogMapper::toDTO).toList();
        ctx.status(HttpStatus.OK);
        ctx.json(medicineLogDTOS);
    }

    // Get logs for a specific medicine
    public void getLogsByMedicine(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        MedicineLog medicineLog = medicineLogDAO.getById(id);
        if(medicineLog != null){
            ctx.status(200);
            ctx.json(MedicineLogMapper.toDTO(medicineLog));
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.result("MedicineLog not found");
        }
    }

    // Create a log
    public void createLog(Context ctx) {
        UserDTO userDTO = ctx.attribute("user");
        if (userDTO == null) {
            ctx.status(HttpStatus.UNAUTHORIZED);
            ctx.result("User not authenticated");
            return;
        }

        int medicineId = Integer.parseInt(ctx.pathParam("medicineId"));
        Medicine medicine = medicineDAO.getById(medicineId);

        if (medicine == null) {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.result("Medicine not found");
            return;
        }

        MedicineLogDTO dto = ctx.bodyAsClass(MedicineLogDTO.class);
        MedicineLog log = MedicineLog.builder()
                .dose(dto.getDose())
                .takenAt(dto.getTakenAt())
                .user(securityDAO.getUserByUsername(userDTO.getUsername()))
                .medicine(medicine)
                .build();

        MedicineLog saved = medicineLogDAO.create(log);
        ctx.status(HttpStatus.CREATED).json(MedicineLogMapper.toDTO(saved));
    }


    // Update a log (fx ændre dosis)
    public void updateLog(Context ctx) {
     try {

        int id = Integer.parseInt(ctx.pathParam("id"));
        MedicineLogDTO medicineLogDTO = ctx.bodyAsClass(MedicineLogDTO.class);

        MedicineLog existingMedicinLog = medicineLogDAO.getById(id);
        if (existingMedicinLog == null) {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.result("Medicine not found");
            return;
        }

        existingMedicinLog.setDose(medicineLogDTO.getDose());
        existingMedicinLog.setTakenAt(medicineLogDTO.getTakenAt());

        MedicineLog updated = medicineLogDAO.update(existingMedicinLog);
         ctx.status(HttpStatus.OK);
         ctx.json(MedicineLogMapper.toDTO(updated));
        } catch (Exception e){
             throw new IllegalStateException("Invalid JSON input: " + e.getMessage(), e);
     }

    }

    // Delete a log
    public void deleteLog(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        boolean delete = medicineLogDAO.delete(id);

        if(delete){
            ctx.result("MedicineLog with id " + id + " deleted");
            ctx.status(HttpStatus.NO_CONTENT);
        } else {
            ctx.result("Medicine not found");
            ctx.status(HttpStatus.NOT_FOUND);
        }
    }

}
