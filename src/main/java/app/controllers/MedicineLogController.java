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
        UserDTO userDTO = ctx.attribute("user");
        if (userDTO == null) {
            ctx.status(HttpStatus.UNAUTHORIZED).result("User not authenticated");
            return;
        }

        List<MedicineLog> logs;
        if (userDTO.getRoles().contains("ADMIN")) {
            logs = medicineLogDAO.getAll();
        } else {
            logs = medicineLogDAO.getByUsername(userDTO.getUsername());
        }

        ctx.json(logs.stream().map(MedicineLogMapper::toDTO).toList());
        ctx.status(HttpStatus.OK);
    }
    // Get logs for a specific medicine
    public void getLogsByMedicine(Context ctx) {
        UserDTO userDTO = ctx.attribute("user");
        if (userDTO == null) {
            ctx.status(HttpStatus.UNAUTHORIZED).result("User not authenticated");
            return;
        }

        int id = Integer.parseInt(ctx.pathParam("id"));
        MedicineLog log = medicineLogDAO.getById(id);

        if (log == null) {
            ctx.status(HttpStatus.NOT_FOUND).result("MedicineLog not found");
            return;
        }

        // Kun ADMIN eller ejer må se loggen
        if (!userDTO.getRoles().contains("ADMIN") &&
                !log.getUser().getUsername().equals(userDTO.getUsername())) {
            ctx.status(HttpStatus.FORBIDDEN).result("Access denied");
            return;
        }

        ctx.status(HttpStatus.OK).json(MedicineLogMapper.toDTO(log));
    }

    // Create a log
    public void createLog(Context ctx) {
        // Hent den loggede bruger
        UserDTO userDTO = ctx.attribute("user");
        if (userDTO == null) {
            ctx.status(HttpStatus.UNAUTHORIZED);
            ctx.result("User not authenticated");
            return;
        }

        // Hent DTO fra JSON-body
        MedicineLogDTO dto = ctx.bodyAsClass(MedicineLogDTO.class);

        // Hent medicinen via navn fra body
        Medicine medicine = medicineDAO.getByName(dto.getMedicineName());
        if (medicine == null) {
            ctx.status(HttpStatus.NOT_FOUND).result("Medicine not found");
            return;
        }

        // Opret log
        MedicineLog log = MedicineLog.builder()
                .dose(dto.getDose())
                .takenAt(dto.getTakenAt())
                .user(securityDAO.getUserByUsername(userDTO.getUsername()))
                .medicine(medicine)
                .build();

        // Gem log i DB
        MedicineLog saved = medicineLogDAO.create(log);

        // Returnér gemt log som DTO
        ctx.status(HttpStatus.CREATED).json(MedicineLogMapper.toDTO(saved));
    }


    // Update a log (fx ændre dosis)
    public void updateLog(Context ctx) {
        UserDTO userDTO = ctx.attribute("user");
        if (userDTO == null) {
            ctx.status(HttpStatus.UNAUTHORIZED).result("User not authenticated");
            return;
        }

        int id = Integer.parseInt(ctx.pathParam("id"));
        MedicineLog existingLog = medicineLogDAO.getById(id);

        if (existingLog == null) {
            ctx.status(HttpStatus.NOT_FOUND).result("MedicineLog not found");
            return;
        }

        // Tjek ejer
        if (!userDTO.getRoles().contains("ADMIN") &&
                !existingLog.getUser().getUsername().equals(userDTO.getUsername())) {
            ctx.status(HttpStatus.FORBIDDEN).result("You cannot edit someone else's log");
            return;
        }

        MedicineLogDTO dto = ctx.bodyAsClass(MedicineLogDTO.class);
        existingLog.setDose(dto.getDose());
        existingLog.setTakenAt(dto.getTakenAt());

        MedicineLog updated = medicineLogDAO.update(existingLog);
        ctx.status(HttpStatus.OK).json(MedicineLogMapper.toDTO(updated));
    }

    // Delete a log
    public void deleteLog(Context ctx) {
        UserDTO userDTO = ctx.attribute("user");
        if (userDTO == null) {
            ctx.status(HttpStatus.UNAUTHORIZED).result("User not authenticated");
            return;
        }

        int id = Integer.parseInt(ctx.pathParam("id"));
        MedicineLog log = medicineLogDAO.getById(id);

        if (log == null) {
            ctx.status(HttpStatus.NOT_FOUND).result("MedicineLog not found");
            return;
        }

        if (!userDTO.getRoles().contains("ADMIN") &&
                !log.getUser().getUsername().equals(userDTO.getUsername())) {
            ctx.status(HttpStatus.FORBIDDEN).result("You cannot delete someone else's log");
            return;
        }

        boolean deleted = medicineLogDAO.delete(id);
        if (deleted) {
            ctx.status(HttpStatus.NO_CONTENT);
        } else {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result("Failed to delete");
        }
    }

}
