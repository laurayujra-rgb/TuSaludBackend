package bo.com.edu.diplomado.tuSaliud.Auth.Dto.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor    // 👈 Jackson necesita ctor vacío
@AllArgsConstructor
public class LoginRequest {

    @NotBlank
    @Email
    @JsonProperty("accountEmail")     // 👈 asegura el nombre con el JSON
    private String accountEmail;

    @NotBlank
    @JsonProperty("accountPassword")  // 👈 asegura el nombre con el JSON
    private String accountPassword;
}