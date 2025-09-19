package bo.com.edu.diplomado.tuSaliud.Auth.Service;

import bo.com.edu.diplomado.tuSaliud.Entity.*;
import bo.com.edu.diplomado.tuSaliud.Models.Dto.AccountResponse;
import bo.com.edu.diplomado.tuSaliud.Auth.Dto.Request.UserRegisterRequest;
import bo.com.edu.diplomado.tuSaliud.Repository.AccountsRepository;
import bo.com.edu.diplomado.tuSaliud.Repository.GendersRepository;
import bo.com.edu.diplomado.tuSaliud.Repository.PersonsRepository;
import bo.com.edu.diplomado.tuSaliud.Repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;   // 👈 IMPORTANTE
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserRegisterService {

    @Autowired
    private final PersonsRepository personsRepo;
    private final AccountsRepository accountsRepo;
    private final GendersRepository gendersRepo;
    private final RolesRepository rolesRepo;
    private final PasswordEncoder passwordEncoder;   // 👈 inyectamos

    public UserRegisterService(PersonsRepository personsRepo,
                               AccountsRepository accountsRepo,
                               GendersRepository gendersRepo,
                               RolesRepository rolesRepo,
                               PasswordEncoder passwordEncoder) {
        this.personsRepo = personsRepo;
        this.accountsRepo = accountsRepo;
        this.gendersRepo = gendersRepo;
        this.rolesRepo = rolesRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AccountResponse register(UserRegisterRequest req) {
        // Validaciones básicas
        if (accountsRepo.existsByAccountEmail(req.account().accountEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ya está en uso");
        }

        // 1) Crear persona
        PersonsEntity person = new PersonsEntity();
        person.setPersonName(req.person().personName());
        person.setPersonFatherSurname(req.person().personFatherSurname());
        person.setPersonMotherSurname(req.person().personMotherSurname());
        person.setPersonDni(req.person().personDni());
        person.setPersonBirthdate(req.person().personBirthdate());
        person.setPersonAge(req.person().personAge());
        person.setGender(gendersRepo.findById(req.person().genderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Género no existe")));
        person.setRole(rolesRepo.findById(req.person().roleId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol no existe")));

        personsRepo.save(person);

        // 2) Crear cuenta asociada
        if (accountsRepo.existsByPerson_PersonId(person.getPersonId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La persona ya tiene una cuenta");
        }

        AccountsEntity acc = new AccountsEntity();
        acc.setPerson(person);
        acc.setAccountEmail(req.account().accountEmail());

        // ✅ Guardar contraseña encriptada con BCrypt
        String hashedPassword = passwordEncoder.encode(req.account().accountPassword());
        acc.setAccountPassword(hashedPassword);

        acc.setAccountStatus(1);

        AccountsEntity saved = accountsRepo.save(acc);

        // 3) Respuesta
        return new AccountResponse(
                saved.getAccountId(),
                saved.getAccountEmail(),
                saved.getAccountStatus(),
                person.getPersonId()
        );
    }
}
