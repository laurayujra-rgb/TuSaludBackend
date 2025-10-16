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

    // 🔹 Información de dieta
    private Long dietId;
    private String dietName;

    // 🔹 Información de paciente
    private Long patientId;
    private String patientName;

    // ❌ Eliminado nurse
    // private Long nurseId;
    // private String nurseName;
}
