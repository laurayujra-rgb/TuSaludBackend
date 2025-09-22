package bo.com.edu.diplomado.tuSaliud.Entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Kardex")
public class KardexEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long kardexId;

    private Integer kardexNumber;
    private String kardexDiagnosis;
    private String kardexDate;
    private String kardexHour;
    private Integer kardexStatus;

    @ManyToOne
    @JoinColumn(name = "diet_id", nullable = false)
    private DietsEntity diets;

    private String nursingActions;

    @PrePersist
    public void prePersist() {
        this.kardexStatus = 1;
    }

}
