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
@ToString(exclude = {"parent", "children"})
@Entity
@Table(name = "diets") // Recomendado en minúsculas para PostgreSQL
public class DietsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long dietId;

    private String dietName;
    private Integer dietStatus;


    @PrePersist
    public void prePersist() {
        this.dietStatus = 1;
    }
}
