package app.controllers;

import Security.daos.SecurityDAO;
import app.DAO.MedicineDAO;
import app.config.HibernateConfig;
import app.dtos.MedicineDTO;
import app.entities.Medicine;
import app.mappers.MedicineMapper;
import dk.bugelhartmann.UserDTO;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class MedicineController {
    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private MedicineDAO medicineDAO = new MedicineDAO(emf);
    private SecurityDAO securityDAO = new SecurityDAO(emf);

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

    public void createMedicine(Context ctx) {
        UserDTO userDTO = ctx.attribute("user"); // kommer fra SecurityController.authenticate()
        if (userDTO == null) {
            ctx.status(HttpStatus.UNAUTHORIZED);
            ctx.result("User not authenticated");
            return;
        }

        MedicineDTO dto = ctx.bodyAsClass(MedicineDTO.class);

        Medicine medicine = Medicine.builder()
                .name(dto.getName())
                .type(dto.getType())
                .symptomDescription(dto.getSymptomDescription())
                .user(securityDAO.getUserByUsername(userDTO.getUsername())) // hent user entity fra DB
                .build();

        Medicine saved = medicineDAO.create(medicine);
        ctx.status(HttpStatus.CREATED).json(MedicineMapper.toDTO(saved));
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

}
