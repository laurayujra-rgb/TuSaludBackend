package bo.com.edu.diplomado.tuSaliud.Models.Dto;
import lombok.*;


@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VitalSignsDto {
    private Long vitalSignsId;
    private String vitalSignsDate;
    private String vitalSignsHour;
    private String vitalSignsTemperature;
    private String vitalSignsHeartRate;
    private String vitalSignsRespiratoryRate;
    private String vitalSignsBloodPressure;
    private String vitalSignsOxygenSaturation;
    private Integer vitalSignsStatus;
    private Long kardexId;
}