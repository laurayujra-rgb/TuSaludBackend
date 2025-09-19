package bo.com.edu.diplomado.tuSaliud.Auth.Service;

import bo.com.edu.diplomado.tuSaliud.Auth.Dto.Request.LoginRequest;
import bo.com.edu.diplomado.tuSaliud.Entity.AccountsEntity;
import bo.com.edu.diplomado.tuSaliud.Models.Dto.AccountResponse;
import bo.com.edu.diplomado.tuSaliud.Repository.AccountsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthLoginService {

    private final AccountsRepository accountsRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthLoginService(AccountsRepository accountsRepo, PasswordEncoder passwordEncoder) {
        this.accountsRepo = accountsRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public AccountResponse login(LoginRequest req) {
        AccountsEntity acc = accountsRepo.findByAccountEmail(req.getAccountEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        if (acc.getAccountStatus() != null && acc.getAccountStatus() == 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cuenta inactiva");
        }

        if (!passwordEncoder.matches(req.getAccountPassword(), acc.getAccountPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        return new AccountResponse(
                acc.getAccountId(),
                acc.getAccountEmail(),
                acc.getAccountStatus(),
                acc.getPerson().getPersonId()
        );
    }
}