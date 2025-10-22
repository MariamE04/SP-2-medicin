package Security.entities;

import app.entities.Medicine;
import app.entities.MedicineLog;
import jakarta.persistence.*;
import lombok.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder

@Entity
@Table(name="users")
public class User {
    @Id
    @Column(name = "username", nullable = false)
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "username"),
            inverseJoinColumns = @JoinColumn(name = "rolename")
    )
    private Set<Role> roles = new HashSet<>();


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicineLog> logs;

    @OneToMany(mappedBy = "user")
    private List<Medicine> medicines;



    public User() {
    }

    public User(String username, String password) {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
        this.username = username;
        this.password = hashed;
    }

    public boolean checkPassword(String candidate) {
        if (BCrypt.checkpw(candidate, password))
            return true;
        else
            return false;
    }

    public void addRole(Role role){
        this.roles.add(role);
        role.getUsers().add(this);
    }
}