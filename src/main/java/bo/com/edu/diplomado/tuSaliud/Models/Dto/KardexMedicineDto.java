package bo.com.edu.diplomado.tuSaliud.Models.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KardexMedicineDto {
    private Long id;
    private Long kardexId;
    private Long medicineId;
    private String medicineName;   // opcional, para mostrar nombre
    private String dose;
    private String frequency;
    private String routeNote;
    private String notes;
    private Integer status;
    private String nurseLic;
}
