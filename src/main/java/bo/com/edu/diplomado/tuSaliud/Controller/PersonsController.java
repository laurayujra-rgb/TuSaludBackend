package bo.com.edu.diplomado.tuSaliud.Controller;

import bo.com.edu.diplomado.tuSaliud.Entity.GendersEntity;
import bo.com.edu.diplomado.tuSaliud.Entity.PersonsEntity;
import bo.com.edu.diplomado.tuSaliud.Entity.RolesEntity;
import bo.com.edu.diplomado.tuSaliud.Models.Response.ApiResponse;
import bo.com.edu.diplomado.tuSaliud.Service.GendersService;
import bo.com.edu.diplomado.tuSaliud.Service.PersonsService;
import bo.com.edu.diplomado.tuSaliud.Service.RolesService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/persons")
public class PersonsController extends ApiController {

    @Autowired
    public GendersService gendersService;
    @Autowired
    public RolesService rolesService;
    @Autowired
    public PersonsService personsService;

    @GetMapping("/all")
    public ApiResponse<List<PersonsEntity>> getAllPersons() {
        ApiResponse<List<PersonsEntity>> response = new ApiResponse<>();
        List<PersonsEntity> persons = personsService.gettAllPersons();
        response.setData(persons);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(HttpStatus.OK.getReasonPhrase());
        return logApiResponse(response);
    }

    @GetMapping
    public ApiResponse<List<PersonsEntity>> getAllPersonsByStatus() {
        ApiResponse<List<PersonsEntity>> response = new ApiResponse<>();
        List<PersonsEntity> persons = personsService.getAllPersonsByStatus();
        response.setData(persons);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(HttpStatus.OK.getReasonPhrase());
        return logApiResponse(response);
    }

    @GetMapping("/{id}")
    public ApiResponse<PersonsEntity> getPersonById(@PathVariable Long id) {
        ApiResponse<PersonsEntity> response = new ApiResponse<>();
        try {
            Optional<PersonsEntity> optionalPerson = personsService.getPersonById(id);
            if (optionalPerson.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("La persona no fue encontrada");
                return logApiResponse(response);
            }
            response.setData(optionalPerson.get());
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Error al obtener persona");
        }
        return logApiResponse(response);
    }

    @GetMapping("/role/{roleId}")
    public ApiResponse<List<PersonsEntity>> getPersonsByRoleId(@PathVariable Long roleId) {
        ApiResponse<List<PersonsEntity>> response = new ApiResponse<>();
        try {
            List<PersonsEntity> persons = personsService.getPersonsByRoleId(roleId);
            if (persons.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("No se encontraron personas con este rol");
                return logApiResponse(response);
            }
            response.setData(persons);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Error al obtener personas por rol");
        }
        return logApiResponse(response);
    }

    @PostMapping("/create")
    public ApiResponse<Optional<PersonsEntity>> createPerson(@RequestBody PersonsEntity personsEntity) {
        ApiResponse<Optional<PersonsEntity>> response = new ApiResponse<>();
        try {
            // Validaciones básicas para evitar NPE
            if (personsEntity.getGender() == null || personsEntity.getGender().getGenderId() == 0) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("Debe especificar un género válido");
                return logApiResponse(response);
            }
            if (personsEntity.getRole() == null || personsEntity.getRole().getRoleId() == 0) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("Debe especificar un rol válido");
                return logApiResponse(response);
            }

            // Validar y setear entidades administradas (Gender / Role)
            Optional<GendersEntity> gender = gendersService.getGenderById(personsEntity.getGender().getGenderId());
            if (gender.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("Gender was not found");
                return logApiResponse(response);
            }
            Optional<RolesEntity> role = rolesService.getRolesById(personsEntity.getRole().getRoleId());
            if (role.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("Role was not found");
                return logApiResponse(response);
            }

            // Construir entidad a persistir (conservar cama enviada si corresponde)
            PersonsEntity person = new PersonsEntity();
            person.setPersonName(personsEntity.getPersonName());
            person.setPersonFatherSurname(personsEntity.getPersonFatherSurname());
            person.setPersonMotherSurname(personsEntity.getPersonMotherSurname());
            person.setPersonDni(personsEntity.getPersonDni());
            person.setPersonBirthdate(personsEntity.getPersonBirthdate());
            person.setPersonAge(personsEntity.getPersonAge());
            person.setPersonStatus(personsEntity.getPersonStatus()); // puede venir null y PrePersist lo setea a 1
            person.setGender(gender.get());
            person.setRole(role.get());

            // Si es paciente (4), permitimos que el body traiga bed { bedId }
            if (role.get().getRoleId() == 4) {
                person.setBed(personsEntity.getBed()); // el Service valida existencia/ocupación
            } else {
                person.setBed(null);
            }

            Optional<PersonsEntity> created = personsService.createPerson(person);
            response.setData(created);
            response.setStatus(HttpStatus.CREATED.value());
            response.setMessage("Creado");
        } catch (ConstraintViolationException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Restricción violada");
        } catch (RuntimeException e) {
            // Errores de negocio desde el Service (cama ocupada, inexistente, etc.)
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Error al crear persona");
        }
        return logApiResponse(response);
    }

    @PutMapping("/update/{id}")
    public ApiResponse<Optional<PersonsEntity>> updatePerson(@PathVariable Long id, @RequestBody PersonsEntity personsEntity) {
        ApiResponse<Optional<PersonsEntity>> response = new ApiResponse<>();
        try {
            Optional<PersonsEntity> existingPerson = personsService.getPersonById(id);
            if (existingPerson.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("Person was not found");
                return logApiResponse(response);
            }

            // Validaciones de entidades relacionadas
            if (personsEntity.getGender() == null || personsEntity.getGender().getGenderId() == 0) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("Debe especificar un género válido");
                return logApiResponse(response);
            }
            if (personsEntity.getRole() == null || personsEntity.getRole().getRoleId() == 0) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("Debe especificar un rol válido");
                return logApiResponse(response);
            }

            Optional<GendersEntity> gender = gendersService.getGenderById(personsEntity.getGender().getGenderId());
            if (gender.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("gender was not found");
                return logApiResponse(response);
            }
            Optional<RolesEntity> role = rolesService.getRolesById(personsEntity.getRole().getRoleId());
            if (role.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("role was not found");
                return logApiResponse(response);
            }

            // Construimos entidad con los cambios (incluida la posible cama)
            PersonsEntity updatePersons = new PersonsEntity();
            updatePersons.setPersonName(personsEntity.getPersonName());
            updatePersons.setPersonFatherSurname(personsEntity.getPersonFatherSurname());
            updatePersons.setPersonMotherSurname(personsEntity.getPersonMotherSurname());
            updatePersons.setPersonDni(personsEntity.getPersonDni());
            updatePersons.setPersonBirthdate(personsEntity.getPersonBirthdate());
            updatePersons.setPersonAge(personsEntity.getPersonAge());
            updatePersons.setPersonStatus(personsEntity.getPersonStatus());
//            personStatu =1
            updatePersons.setPersonStatus(1);
            updatePersons.setGender(gender.get());
            updatePersons.setRole(role.get());


            // Si es paciente, permitimos cama (service valida disponibilidad / libera la anterior)
            if (role.get().getRoleId() == 4) {
                updatePersons.setBed(personsEntity.getBed()); // puede venir null si no desea cambiar la cama
            } else {
                updatePersons.setBed(null); // si deja de ser paciente, se libera en el service
            }

            Optional<PersonsEntity> updateEntity = personsService.updatePerson(id, updatePersons);
            response.setData(updateEntity);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (RuntimeException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Error al actualizar persona");
        }
        return logApiResponse(response);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Optional<PersonsEntity>> deletePerson(@PathVariable Long id) {
        ApiResponse<Optional<PersonsEntity>> response = new ApiResponse<>();
        try {
            Optional<PersonsEntity> existingPerson = personsService.getPersonById(id);
            if (existingPerson.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("person was not found");
                return logApiResponse(response);
            }
            Optional<PersonsEntity> deleteEntity = personsService.deletePerson(id);
            response.setData(deleteEntity);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (RuntimeException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Error al eliminar persona");
        }
        return logApiResponse(response);
    }


    @GetMapping("/rooms/{roomId}/patients")
    public ApiResponse<List<PersonsEntity>> getPatientsByRoom(@PathVariable Long roomId) {
        ApiResponse<List<PersonsEntity>> response = new ApiResponse<>();
        try {
            List<PersonsEntity> patients = personsService.getPatientsByRoomId(roomId);
            if (patients.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("No se encontraron pacientes en esta sala");
                return logApiResponse(response);
            }
            response.setData(patients);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Error al obtener pacientes por sala");
        }
        return logApiResponse(response);
    }
}
