package bo.com.edu.diplomado.tuSaliud.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Persons")
public class PersonsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long personId;

    private String personName;
    private String personFatherSurname;
    private String personMotherSurname;
    private String personDni;
    private String personBirthdate;
    private Integer personAge;
    private Integer personStatus;

    @ManyToOne
    @JoinColumn(name = "gender_id", nullable = false)
    private GendersEntity gender;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private RolesEntity role;

    @PrePersist
    public void prePersist() {
        this.personStatus = 1;
    }
}
