package bo.com.edu.diplomado.tuSaliud.Entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
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
    private String medicineDateToEnd;
    private String medicineMoorning;
    private String medicineAfternoon;
    private String medicineEvening;

    private Integer medicineStatus;

    @ManyToOne
    @JoinColumn(name = "via_id", nullable = false)
    private ViaEntity via;


    @PrePersist
    public void prePersist() {
        this.medicineStatus = 1;
    }


}
