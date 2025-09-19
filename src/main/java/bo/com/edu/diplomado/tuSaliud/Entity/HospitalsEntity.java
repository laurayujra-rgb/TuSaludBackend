package bo.com.edu.diplomado.tuSaliud.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "hospitals")
@Entity
@Table(name = "Hospitals")
public class HospitalsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long hospitalId;
    private  String hospitalName;
    private Integer hospitalStatus;

    @PrePersist
    public void prePersist() {
        this.hospitalStatus = 1;
    }
}
