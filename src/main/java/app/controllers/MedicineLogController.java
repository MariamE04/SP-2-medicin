package app.controllers;

import app.DAO.MedicineLogDAO;
import app.config.HibernateConfig;
import app.dtos.MedicineDTO;
import app.dtos.MedicineLogDTO;
import app.entities.MedicineLog;
import app.mappers.MedicineLogMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class MedicineLogController {

    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private MedicineLogDAO medicineLogDAO = new MedicineLogDAO(emf);

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
        String body = ctx.body().trim();
        ObjectMapper mapper = new ObjectMapper();

        try{
            List<MedicineLogDTO> dtos;

            if(body.startsWith("[")){
                dtos = mapper.readValue(body, new TypeReference<List<MedicineLogDTO>>() {
                });
            } else {
                dtos = List.of(mapper.readValue(body, MedicineLogDTO.class));
            }

            List<MedicineLogDTO> saved = new ArrayList<>();
            for(MedicineLogDTO dto: dtos){
                MedicineLog medicineLog = MedicineLog.builder()
                        .dose(dto.getDose())
                        .takenAt(dto.getTakenAt())
                        .build();

                MedicineLog persistedMedicineLog = medicineLogDAO.creat(medicineLog);
                saved.add(MedicineLogMapper.toDTO(persistedMedicineLog));
            }
            if (saved.size() == 1) {
                ctx.status(201).json(saved.get(0));
            } else {
                ctx.status(201).json(saved);
            }

        }catch (Exception e) {
            throw new IllegalStateException("Invalid JSON input: " + e.getMessage(), e);
        }
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
