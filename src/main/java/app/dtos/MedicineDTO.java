package app.dtos;

import app.entities.MedicineLog;

import java.util.List;

public class MedicineDTO {
    private int id;
    private String name;
    private String type;
    private String symptomDescription;
    private List<MedicineLog> logs;
}
