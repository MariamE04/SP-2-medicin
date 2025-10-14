package app.entities;

import Security.entities.User;
import jakarta.persistence.*;
import lombok.ToString;

import java.util.List;

@Entity
public class Medicine {
    @Id
    @GeneratedValue
    private int id;

    private String name;
    private String type;  // fx antidepressant, painkiller, vitamin
    private String symptomDescription; // fx angst, migraene osv

    @OneToMany(mappedBy = "medicine")
    @ToString.Exclude
    private List<MedicineLog> logs;

    @ManyToOne
    private User user;

}
