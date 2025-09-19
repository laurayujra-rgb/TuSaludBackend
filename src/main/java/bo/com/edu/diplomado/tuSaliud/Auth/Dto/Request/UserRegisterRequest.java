package bo.com.edu.diplomado.tuSaliud.Auth.Dto.Request;

import jakarta.validation.constraints.*;
import org.antlr.v4.runtime.misc.NotNull;

public record UserRegisterRequest(
        @NotNull PersonPart person,
        @NotNull AccountPart account
) {
    public record PersonPart(
            @NotBlank String personName,
            @NotBlank String personFatherSurname,
            String personMotherSurname,
            @NotBlank String personDni,
            @NotBlank String personBirthdate, // si luego pasas a LocalDate mejor
            @NotNull Integer personAge,
            @NotNull Long genderId,
            @NotNull Long roleId
    ) {}

    public record AccountPart(
            @NotBlank @Email String accountEmail,
            @NotBlank @Size(min = 6) String accountPassword
    ) {}
}