package app.mappers;

import app.dtos.MedicineLogDTO;
import app.entities.MedicineLog;

public class MedicineLogMapper {

    public static MedicineLogDTO toDTO(MedicineLog log) {
        return new MedicineLogDTO(
                log.getId(),
                log.getDose(),
                log.getTakenAt(),
                log.getUser() != null ? log.getUser().getUsername() : null,
                log.getMedicine() != null ? log.getMedicine().getName() : null
        );
    }

    public static MedicineLog toEntity(MedicineLogDTO dto) {
        MedicineLog log = new MedicineLog();
        log.setDose(dto.getDose());
        log.setTakenAt(dto.getTakenAt());
        return log;
    }
}
