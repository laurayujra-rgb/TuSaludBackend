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
    private String personBirthdate; // considera LocalDate en el futuro
    private Integer personAge;
    private Integer personStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender_id", nullable = false, referencedColumnName = "genderId")
    private GendersEntity gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false, referencedColumnName = "roleId")
    private RolesEntity role;

    @PrePersist
    public void prePersist() {
        this.personStatus = 1;
    }
}
