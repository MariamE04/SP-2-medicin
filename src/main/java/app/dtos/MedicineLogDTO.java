package app.dtos;

import Security.entities.User;
import app.entities.Medicine;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MedicineLogDTO {
    private int id;
    private double dose;
    private LocalDateTime takenAt;
    private String username;
    private String medicineName; //TODO: find ud af om det skal være Medicine eller string

    public MedicineLogDTO(int id, double dose, LocalDateTime takenAt, String username, String medicineName) {
        this.id = id;
        this.dose = dose;
        this.takenAt = takenAt;
        this.username = username;
        this.medicineName = medicineName;
    }
}
