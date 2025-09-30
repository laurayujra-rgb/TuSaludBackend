package bo.com.edu.diplomado.tuSaliud.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Reports")
public class ReportsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    private Integer reportStatus;


    @Column(columnDefinition = "TEXT") // Permite texto largo
    private String reportDetails;

    // Relación con Kardex
    @ManyToOne
    @JoinColumn(name = "kardex_id", nullable = false)

    private KardexEntity kardex;

    @PrePersist
    public void prePersist() {
        this.reportStatus = 1; // activo por defecto
    }
}
