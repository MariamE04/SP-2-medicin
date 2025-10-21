package app.mappers;

import app.dtos.MedicineDTO;
import app.entities.Medicine;
import app.entities.MedicineLog;

import java.util.stream.Collectors;

public class MedicineMapper {

    public static MedicineDTO toDTO(Medicine medicine) {
        MedicineDTO dto = new MedicineDTO(
                medicine.getId(),
                medicine.getName(),
                medicine.getType(),
                medicine.getSymptomDescription()
        );

        if (medicine.getLogs() != null) {
            dto.setLogIds(
                    medicine.getLogs().stream()
                            .map(MedicineLog::getId)
                            .collect(Collectors.toList())
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