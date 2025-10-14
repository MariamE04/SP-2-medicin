package app.dtos;

import Security.entities.User;
import app.entities.Medicine;

import java.time.LocalDateTime;

public class MedicineLogDTO {
    private int id;
    private double dose;
    private LocalDateTime takenAt;
    private User user;
    private Medicine medicine;
}
