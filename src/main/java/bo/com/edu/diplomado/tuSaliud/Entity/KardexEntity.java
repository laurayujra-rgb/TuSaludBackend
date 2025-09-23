package bo.com.edu.diplomado.tuSaliud.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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

    @JsonIgnore
    @OneToMany(mappedBy = "kardex", fetch = FetchType.LAZY)
    private List<VitalSignsEntity> vitalSigns;

    @ManyToOne
    @JoinColumn(name = "diet_id", nullable = false)
    private DietsEntity diets;

    @OneToMany(mappedBy = "kardex", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<KardexMedicinesEntity> kardexMedicines = new java.util.ArrayList<>();

    private String nursingActions;

    @PrePersist
    public void prePersist() {
        this.kardexStatus = 1;
    }

}
