package bo.com.edu.diplomado.tuSaliud.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    // Relación con paciente
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private PersonsEntity patient;

    // Relación con enfermera
    @ManyToOne
    @JoinColumn(name = "nurse_id", nullable = false)
    private PersonsEntity nurse;

    // Relación con signos vitales
    @JsonIgnore
    @OneToMany(mappedBy = "kardex", fetch = FetchType.LAZY)
    private List<VitalSignsEntity> vitalSigns;

    // Relación con dietas
    @ManyToOne
    @JoinColumn(name = "diet_id", nullable = false)
    private DietsEntity diets;

    // Relación con medicamentos
    @OneToMany(mappedBy = "kardex", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KardexMedicinesEntity> kardexMedicines = new ArrayList<>();

    // Relación con reportes
    @OneToMany(mappedBy = "kardex", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportsEntity> reports = new ArrayList<>();

    private String nursingActions;

    @PrePersist
    public void prePersist() {
        this.kardexStatus = 1;
    }
}
