package bo.com.edu.diplomado.tuSaliud.Models.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReportsDto {
    private Long reportId;
    private Integer reportStatus;
    private String reportDetails;
    private Long kardexId; // Solo el ID del Kardex
}
