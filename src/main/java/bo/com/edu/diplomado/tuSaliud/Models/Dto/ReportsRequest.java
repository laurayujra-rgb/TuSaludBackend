package bo.com.edu.diplomado.tuSaliud.Models.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportsRequest {
    private String reportDetails;
    private Long kardexId;
}
