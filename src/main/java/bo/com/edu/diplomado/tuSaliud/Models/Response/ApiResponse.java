package bo.com.edu.diplomado.tuSaliud.Models.Response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

@Data
public class ApiResponse <T> {
    @JsonIgnore
    @ToString.Exclude
    private String uuid;
    private Integer status = 0;
    private String message = "";
    private T data;
    private String error = "";
}
