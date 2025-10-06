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

    // Dieta
    private Long dietId;
    private String dietName;

    // Paciente y enfermera
    private Long patientId;
    private String patientName;
    private Long nurseId;
    private String nurseName;

    // 🔹 Nueva info de cama/sala
    private Long bedId;
    private String bedName;
    private Long roomId;
    private String roomName;
}
