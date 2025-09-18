// Controller/UserRegisterController.java
package bo.com.edu.diplomado.tuSaliud.Controller;


import bo.com.edu.diplomado.tuSaliud.Models.Dto.AccountResponse;
import bo.com.edu.diplomado.tuSaliud.Models.Dto.UserRegisterRequest;
import bo.com.edu.diplomado.tuSaliud.Service.UserRegisterService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserRegisterController {

    private final UserRegisterService service;

    public UserRegisterController(UserRegisterService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public AccountResponse register(@Valid @RequestBody UserRegisterRequest req) {
        return service.register(req);
    }
}
