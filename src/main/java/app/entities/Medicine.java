package app.entities;

import Security.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

@Entity
public class Medicine {
    @Id
    @GeneratedValue
    private int id;

    private String name;
    private String type;  // fx antidepressant, painkiller, vitamin
    private String symptomDescription; // fx angst, migraene osv

    @OneToMany(mappedBy = "medicine", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<MedicineLog> logs;

    @ManyToOne
    private User user;

}
