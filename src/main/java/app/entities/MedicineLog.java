package app.entities;

import Security.entities.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class MedicineLog {
    @Id
    @GeneratedValue
    private int id;

    private double dose; // fx 25.0 mg
    private LocalDateTime takenAt;

    @ManyToOne
    private User user;

    @ManyToOne
    private Medicine medicine;
}
