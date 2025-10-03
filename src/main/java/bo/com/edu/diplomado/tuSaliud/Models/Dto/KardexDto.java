package bo.com.edu.diplomado.tuSaliud.Models.Dto;



import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KardexDto {
    private Long kardexId;
    private Integer kardexNumber;
    private String kardexDiagnosis;
    private String kardexDate;
    private String kardexHour;
    private Integer kardexStatus;
    private String nursingActions;

    // Dieta (simplificada)
    private Long dietId;
    private String dietName;

    private Long patientId;
    private String patientName;

    private Long nurseId;
    private String nurseName;
}
