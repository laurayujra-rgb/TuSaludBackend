package bo.com.edu.diplomado.tuSaliud.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Vital_Signs")
public class VitalSignsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long vitalSignsId;

    private String VitalSignsDate;
    private String VitalSignsHour;
    private String VitalSignsTemperature;
    private String VitalSignsHeartRate;
    private String VitalSignsRespiratoryRate;
    private String VitalSignsBloodPressure;
    private String VitalSignsOxygenSaturation;
    private Integer vitalSignsStatus;
    private String nureseName;

    @ManyToOne
    @JoinColumn(name = "kardex_id", nullable = false)
    @JsonIgnoreProperties({"vitalSigns"})
    private KardexEntity kardex;

    @PrePersist
    public void prePersist(){
        this.vitalSignsStatus = 1;
    }


}
