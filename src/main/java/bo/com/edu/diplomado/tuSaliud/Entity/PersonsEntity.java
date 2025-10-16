package bo.com.edu.diplomado.tuSaliud.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    @JoinColumn(name = "bed_id", nullable = true)
    private BedsEntity bed;


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

    // Relación con cuenta
    @OneToOne(mappedBy = "person", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private AccountsEntity account;

    // Relación: Persona como Paciente en muchos Kardex
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<KardexEntity> kardexAsPatient = new ArrayList<>();

//    // Relación: Persona como Enfermera en muchos Kardex
//    @OneToMany(mappedBy = "nurse", fetch = FetchType.LAZY)
//    @JsonIgnore
//    @ToString.Exclude
//    private List<KardexEntity> kardexAsNurse = new ArrayList<>();
}
