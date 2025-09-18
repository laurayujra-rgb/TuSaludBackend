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
@ToString(exclude = "persons")
@Entity
@Table(name = "Genders")
public class GendersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long genderId;

    private String genderName;
    private Integer genderStatus;

    @JsonIgnore
    @OneToMany(mappedBy = "gender", fetch = FetchType.LAZY)
    private List<PersonsEntity> persons;

    @PrePersist
    public void prePersist() {
        this.genderStatus = 1;
    }
}
