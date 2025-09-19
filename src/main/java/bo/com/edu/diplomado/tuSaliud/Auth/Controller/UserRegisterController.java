// Controller/UserRegisterController.java
package bo.com.edu.diplomado.tuSaliud.Auth.Controller;


import bo.com.edu.diplomado.tuSaliud.Models.Dto.AccountResponse;
import bo.com.edu.diplomado.tuSaliud.Auth.Dto.Request.UserRegisterRequest;
import bo.com.edu.diplomado.tuSaliud.Auth.Service.UserRegisterService;
import bo.com.edu.diplomado.tuSaliud.Models.Response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserRegisterController {

    private final UserRegisterService service;

    public UserRegisterController(UserRegisterService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AccountResponse>> register(@Valid @RequestBody UserRegisterRequest req) {
        AccountResponse result = service.register(req);

        ApiResponse<AccountResponse> api = new ApiResponse<>();
        api.setStatus(HttpStatus.CREATED.value());
        api.setMessage("Registro exitoso");
        api.setData(result);

        return ResponseEntity.ok(api);
    }
}