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

    // ADMIN -> ser alle, USER -> kun egne
    public void getAllMedicine(Context ctx) {
        UserDTO userDTO = ctx.attribute("user");

        if (userDTO == null) {
            ctx.status(HttpStatus.UNAUTHORIZED).result("User not authenticated");
            return;
        }

        List<Medicine> medicines;
        if (userDTO.getRoles().contains("ADMIN")) {
            medicines = medicineDAO.getAll();
        } else {
            medicines = medicineDAO.getByUsername(userDTO.getUsername());
        }

        ctx.json(medicines.stream().map(MedicineMapper::toDTO).toList());
        ctx.status(HttpStatus.OK);
    }

    public void getById(Context ctx){
        UserDTO userDTO = ctx.attribute("user");
        int id = Integer.parseInt(ctx.pathParam("id"));

        Medicine medicine = medicineDAO.getById(id);
        if (medicine == null) {
            ctx.status(HttpStatus.NOT_FOUND).result("Medicine not found");
            return;
        }

        // Hvis ikke admin og ikke ejer af medicine -> forbudt
        if (!userDTO.getRoles().contains("ADMIN") &&
                !medicine.getUser().getUsername().equals(userDTO.getUsername())) {
            ctx.status(HttpStatus.FORBIDDEN).result("Access denied");
            return;
        }

        ctx.json(MedicineMapper.toDTO(medicine));
        ctx.status(HttpStatus.OK);
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
        UserDTO userDTO = ctx.attribute("user");
        int id = Integer.parseInt(ctx.pathParam("id"));
        MedicineDTO dto = ctx.bodyAsClass(MedicineDTO.class);

        Medicine existing = medicineDAO.getById(id);
        if (existing == null) {
            ctx.status(HttpStatus.NOT_FOUND).result("Medicine not found");
            return;
        }

        if (!userDTO.getRoles().contains("ADMIN") &&
                !existing.getUser().getUsername().equals(userDTO.getUsername())) {
            ctx.status(HttpStatus.FORBIDDEN).result("Access denied");
            return;
        }

        existing.setName(dto.getName());
        existing.setType(dto.getType());
        existing.setSymptomDescription(dto.getSymptomDescription());

        Medicine updated = medicineDAO.update(existing);
        ctx.status(HttpStatus.OK).json(MedicineMapper.toDTO(updated));
    }


    public void medicineToDelete(Context ctx){
        UserDTO userDTO = ctx.attribute("user");
        int id = Integer.parseInt(ctx.pathParam("id"));
        Medicine existing = medicineDAO.getById(id);

        if (existing == null) {
            ctx.status(HttpStatus.NOT_FOUND).result("Medicine not found");
            return;
        }

        if (!userDTO.getRoles().contains("ADMIN") &&
                !existing.getUser().getUsername().equals(userDTO.getUsername())) {
            ctx.status(HttpStatus.FORBIDDEN).result("Access denied");
            return;
        }

        medicineDAO.delete(id);
        ctx.status(HttpStatus.NO_CONTENT);
    }

}
