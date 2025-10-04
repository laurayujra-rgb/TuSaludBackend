package bo.com.edu.diplomado.tuSaliud.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "Medicines")
public class MedicinesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long medicineId;

    private String medicineName;
    private String medicineLaboratory;
    private Integer medicineStatus;

    @ManyToOne
    @JoinColumn(name = "via_id", nullable = false)
    @JsonIgnoreProperties({"medicines"}) // 👈 evita recursión desde Via->medicines
    private ViaEntity via;

    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonIgnore // 👈 no serializar la lista para evitar recursión y Lazy init
    private List<KardexMedicinesEntity> kardexMedicines = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.medicineStatus = 1;
    }
}
