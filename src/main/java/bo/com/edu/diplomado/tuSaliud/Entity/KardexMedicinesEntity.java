package bo.com.edu.diplomado.tuSaliud.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(
        name = "kardex_medicines",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_kardex_medicine", columnNames = {"kardex_id", "medicine_id"})
        }
)
public class KardexMedicinesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FKs
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kardex_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_km_kardex"))
    private KardexEntity kardex;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicine_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_km_medicine"))
    private MedicinesEntity medicine;

    // Atributos de la prescripción
    private String dose;       // ej: "500 mg"
    private String frequency;  // ej: "cada 8 horas"
    private String routeNote;  // nota de vía (si quieres)
    private String notes;      // indicaciones

    // Soft-delete igual que tu Kardex
    private Integer status;

    @PrePersist
    public void prePersist() {
        if (status == null) status = 1;
    }
}
