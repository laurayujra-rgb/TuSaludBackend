package bo.com.edu.diplomado.tuSaliud.Auth.Controller;

import bo.com.edu.diplomado.tuSaliud.Auth.Dto.Request.LoginRequest;
import bo.com.edu.diplomado.tuSaliud.Auth.Service.AuthLoginService;
import bo.com.edu.diplomado.tuSaliud.Models.Dto.AccountResponse;
import bo.com.edu.diplomado.tuSaliud.Models.Response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/auth")
public class AuthLoginController {
    private final AuthLoginService service;
    public AuthLoginController(AuthLoginService service) {
        this.service = service;
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AccountResponse>> login(@Valid @RequestBody LoginRequest req) {
        AccountResponse data = service.login(req);
        ApiResponse<AccountResponse> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(HttpStatus.OK.getReasonPhrase());
        response.setMessage("Inicio de sesión exitoso");
        response.setData(data);
        return ResponseEntity.ok(response);
    }
}