package app.mappers;

import app.dtos.MedicineDTO;
import app.entities.Medicine;

public class MedicineMapper {

    public static MedicineDTO toDTO(Medicine medicine) {
        return new MedicineDTO(
                medicine.getId(),
                medicine.getName(),
                medicine.getType(),
                medicine.getSymptomDescription()
        );
    }

    public static Medicine toEntity(MedicineDTO dto) {
        Medicine medicine = new Medicine();
        medicine.setName(dto.getName());
        medicine.setType(dto.getType());
        medicine.setSymptomDescription(dto.getSymptomDescription());
        return medicine;
    }
}