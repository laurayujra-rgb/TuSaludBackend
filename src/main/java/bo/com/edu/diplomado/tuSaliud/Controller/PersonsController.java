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
public class PersonsController extends ApiController{

    @Autowired
    public GendersService gendersService;
    @Autowired
    public RolesService rolesService;
    @Autowired
    public PersonsService personsService;

    @GetMapping("/all")
    public ApiResponse<List<PersonsEntity>> getAllPersons(){
        ApiResponse<List<PersonsEntity>> response = new ApiResponse<>();
        List<PersonsEntity> persons = personsService.gettAllPersons();
        response.setData(persons);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(HttpStatus.OK.getReasonPhrase());
        return logApiResponse(response);
    }
    @GetMapping
    public ApiResponse<List<PersonsEntity>> getAllPersonsByStatus(){
        ApiResponse<List<PersonsEntity>> response = new ApiResponse<>();
        List<PersonsEntity> persons = personsService.getAllPersonsByStatus();
        response.setData(persons);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(HttpStatus.OK.getReasonPhrase());
        return logApiResponse(response);
    }
    @GetMapping("/{id}")
    public ApiResponse<PersonsEntity> getPersonById(@PathVariable Long id){
         ApiResponse<PersonsEntity> response = new ApiResponse<>();
         try{
             Optional<PersonsEntity> optionalPerson = personsService.getPersonById(id);
             if(optionalPerson.isEmpty()){
                 response.setStatus(HttpStatus.BAD_REQUEST.value());
                 response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                 response.setMessage("person was not found");
                 return logApiResponse(response);
             }
                PersonsEntity personsResponse = new PersonsEntity();
                personsResponse.setPersonId(optionalPerson.get().getPersonId());
                personsResponse.setPersonName(optionalPerson.get().getPersonName());
                personsResponse.setPersonFatherSurname(optionalPerson.get().getPersonFatherSurname());
                personsResponse.setPersonMotherSurname(optionalPerson.get().getPersonMotherSurname());
                personsResponse.setPersonDni(optionalPerson.get().getPersonDni());
                personsResponse.setPersonBirthdate(optionalPerson.get().getPersonBirthdate());
                personsResponse.setPersonAge(optionalPerson.get().getPersonAge());
                personsResponse.setPersonStatus(optionalPerson.get().getPersonStatus());
                personsResponse.setGender(personsResponse.getGender());
                personsResponse.setRole(personsResponse.getRole());
                response.setData(personsResponse);
                response.setStatus(HttpStatus.OK.value());
                response.setMessage(HttpStatus.OK.getReasonPhrase());
         }catch (Exception e){
             response.setStatus(HttpStatus.BAD_REQUEST.value());
             response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
         }
         return logApiResponse(response);
    }
    @PostMapping("/create")
    public ApiResponse<Optional<PersonsEntity>> createPerson(@RequestBody PersonsEntity personsEntity){
        ApiResponse<Optional<PersonsEntity>> response = new ApiResponse<>();
        try{
            Optional<GendersEntity> gender = gendersService.getGenderById(personsEntity.getGender().getGenderId());
            if(gender.isEmpty()){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("Gender  was not found");
                return logApiResponse(response);
            }
            Optional<RolesEntity> role = rolesService.getRolesById(personsEntity.getRole().getRoleId());
            if(role.isEmpty()){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("Role  was not found");
                return logApiResponse(response);
            }
            PersonsEntity person = new PersonsEntity();
            person.setPersonName(personsEntity.getPersonName());
            person.setPersonFatherSurname(personsEntity.getPersonFatherSurname());
            person.setPersonMotherSurname(personsEntity.getPersonMotherSurname());
            person.setPersonDni(personsEntity.getPersonDni());
            person.setPersonBirthdate(personsEntity.getPersonBirthdate());
            person.setPersonAge(personsEntity.getPersonAge());
            person.setPersonStatus(personsEntity.getPersonStatus());
            person.setGender(gender.get());
            person.setRole(role.get());
            Optional<PersonsEntity> createPerson = personsService.createPerson(person);
            response.setData(createPerson);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        }catch(ConstraintViolationException e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
    }catch (Exception e){
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }
    @PutMapping("/update/{id}")
    public ApiResponse<Optional<PersonsEntity>> updatePerson(@PathVariable Long id, @RequestBody PersonsEntity personsEntity ){
        ApiResponse<Optional<PersonsEntity>> response = new ApiResponse<>();
        try{
            Optional<PersonsEntity> existingPerson = personsService.getPersonById(id);
            if(existingPerson.isEmpty()){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("person was not found");
                return logApiResponse(response);
            }
//            validate if gender exists
            Optional<GendersEntity>gender = gendersService.getGenderById(personsEntity.getGender().getGenderId());
            if(gender.isEmpty()){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("gender was not found");
            }
//            validate if rol exists
            Optional<RolesEntity>role =  rolesService.getRolesById(personsEntity.getRole().getRoleId());
            if(role.isEmpty()){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("role was not found");
            }
            PersonsEntity updatePersons = new PersonsEntity();
            updatePersons.setPersonName(personsEntity.getPersonName());
            updatePersons.setPersonFatherSurname(personsEntity.getPersonFatherSurname());
            updatePersons.setPersonMotherSurname(personsEntity.getPersonMotherSurname());
            updatePersons.setPersonDni(personsEntity.getPersonDni());
            updatePersons.setPersonBirthdate(personsEntity.getPersonBirthdate());
            updatePersons.setPersonAge(personsEntity.getPersonAge());
            updatePersons.setPersonStatus(personsEntity.getPersonStatus());
            updatePersons.setGender(gender.get());
            updatePersons.setRole(role.get());
            Optional<PersonsEntity> updateEntity = personsService.updatePerson(id, updatePersons);
            response.setData(updateEntity);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Optional<PersonsEntity>> deletePerson(@PathVariable Long id){
        ApiResponse<Optional<PersonsEntity>> response = new ApiResponse<>();
        try{
            Optional<PersonsEntity> existingPerson = personsService.getPersonById(id);
            if(existingPerson.isEmpty()){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("person was not found");
                return logApiResponse(response);
            }
            Optional<PersonsEntity> deleteEntity = personsService.deletePerson(id);
            response.setData(deleteEntity);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }
}
