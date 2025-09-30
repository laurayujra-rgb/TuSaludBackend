package bo.com.edu.diplomado.tuSaliud.Models.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class VitalSignsDto {
    private Long id;
    private String date;
    private String hour;
    private String temperature;
    private String heartRate;
    private String respiratoryRate;
    private String bloodPressure;
    private String oxygenSaturation;
    private Integer status;
    private Long kardexId; // solo el ID, no el objeto completo
}