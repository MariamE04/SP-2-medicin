package app.mappers;

import Security.entities.User;
import app.dtos.MedicineDTO;
import app.entities.Medicine;
import app.entities.MedicineLog;
import dk.bugelhartmann.UserDTO;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class MedicineMapper {

    public static MedicineDTO toDTO(Medicine medicine) {
        MedicineDTO dto = new MedicineDTO(
                medicine.getId(),
                medicine.getName(),
                medicine.getType(),
                medicine.getSymptomDescription()
        );

        // altid en liste, selvom der ikke er logs
        if (medicine.getLogs() != null && !medicine.getLogs().isEmpty()) {
            dto.setLogIds(
                    medicine.getLogs().stream()
                            .map(MedicineLog::getId)
                            .collect(Collectors.toList())
            );
        } else {
            dto.setLogIds(new ArrayList<>()); // tom liste i stedet for null
        }


        if (medicine.getUser() != null) {
            User user = medicine.getUser();

            // konverter Role -> String
            Set<String> roleNames = user.getRoles().stream()
                    .map(role -> role.getRolename()) // eller .toString() afhængigt af din Role-klasse
                    .collect(Collectors.toSet());

            dto.setUser(
                    UserDTO.builder()
                            .username(user.getUsername())
                            .roles(roleNames)
                            .build()
            );

    }

        return dto;
    }

    public static Medicine toEntity(MedicineDTO dto) {
        Medicine medicine = new Medicine();
        medicine.setName(dto.getName());
        medicine.setType(dto.getType());
        medicine.setSymptomDescription(dto.getSymptomDescription());
        return medicine;
    }
}