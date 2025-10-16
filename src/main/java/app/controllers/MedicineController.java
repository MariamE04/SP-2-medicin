package app.controllers;

import app.DAO.MedicineDAO;
import app.config.HibernateConfig;
import app.dtos.MedicineDTO;
import app.dtos.MedicineLogDTO;
import app.entities.Medicine;
import app.mappers.MedicineMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class MedicineController {
    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private MedicineDAO medicineDAO;

    public void getAllMedicine(Context ctx){
        List<Medicine> medicines = medicineDAO.getAll();
        List<MedicineDTO> medicineDTOS = medicines.stream().map(MedicineMapper::toDTO).toList();
        ctx.status(HttpStatus.OK);
        ctx.json(medicineDTOS);
    }

    public void getById(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        Medicine medicine = medicineDAO.getById(id);
        if(medicine != null){
            ctx.status(200);
            ctx.json(MedicineMapper.toDTO(medicine));
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.result("Medicine not found");
        }
    }

    public void creatMedicine(Context ctx) {
        String body = ctx.body().trim();
        ObjectMapper mapper = new ObjectMapper();

        try {
            List<MedicineDTO> dtos;

            // Tjek om det er en liste eller enkelt objekt
            if (body.startsWith("[")) {
                dtos = mapper.readValue(body, new TypeReference<List<MedicineDTO>>() {
                });
            } else {
                dtos = List.of(mapper.readValue(body, MedicineDTO.class));
            }

            List<MedicineDTO> saved = new ArrayList<>();
            for (MedicineDTO dto : dtos) {
                Medicine medicine = Medicine.builder()
                        .name(dto.getName())
                        .type(dto.getType())
                        .symptomDescription(dto.getSymptomDescription())
                        .logs(dto.getLogIds())
                        .build();

                Medicine persistedMedicine = medicineDAO.creat(medicine);
                saved.add(MedicineMapper.toDTO(persistedMedicine));
            }

            if (saved.size() == 1) {
                ctx.status(201).json(saved.get(0));
            } else {
                ctx.status(201).json(saved);
            }

        } catch (Exception e) {
            throw new IllegalStateException("Invalid JSON input: " + e.getMessage(), e);
        }
    }

    public void updateMedicine(Context ctx) {
        try{
            int id = Integer.parseInt(ctx.pathParam("id"));
            MedicineDTO medicineDTO = ctx.bodyAsClass(MedicineDTO.class);

            Medicine existingMedicin = medicineDAO.getById(id);
            if(existingMedicin == null){
                ctx.status(HttpStatus.NOT_FOUND);
                ctx.result("Medicine not found");
                return;
            }

            existingMedicin.setName(medicineDTO.getName());
            existingMedicin.setType(medicineDTO.getType());

            Medicine updated = medicineDAO.update(existingMedicin);
            ctx.status(HttpStatus.OK);
            ctx.json(MedicineMapper.toDTO(updated));
        } catch (Exception e){
            throw new IllegalStateException("Invalid JSON input: " + e.getMessage(), e);
        }
    }

    public void medicineToDelete(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        boolean delete = medicineDAO.delete(id);

        if(delete){
            ctx.result("Medicine with id " + id + " deleted");
            ctx.status(HttpStatus.NO_CONTENT);
        } else {
            ctx.result("Medicine not found");
            ctx.status(HttpStatus.NOT_FOUND);
        }

    }

    /// TODO: getMedicineLogs -Hent alle logs for den medicin

    /// TODO: addMedicineLog -Tilføj log til den medicin

}
