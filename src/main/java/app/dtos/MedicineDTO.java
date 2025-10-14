package app.dtos;

import app.entities.MedicineLog;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor

public class MedicineDTO {
    private int id;
    private String name;
    private String type;
    private String symptomDescription;
    private List<Integer> logIds;

    public MedicineDTO(int id, String name, String type, String symptomDescription) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.symptomDescription = symptomDescription;
    }
}
