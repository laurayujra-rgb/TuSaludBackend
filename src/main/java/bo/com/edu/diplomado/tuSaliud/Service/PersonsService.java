package bo.com.edu.diplomado.tuSaliud.Service;

import bo.com.edu.diplomado.tuSaliud.Entity.PersonsEntity;
import bo.com.edu.diplomado.tuSaliud.Repository.PersonsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PersonsService {
    @Autowired
    private PersonsRepository personsRepository;
    public List<PersonsEntity> gettAllPersons(){
        return personsRepository.findAll();
    }
    public List<PersonsEntity> getAllPersonsByStatus(){
        return personsRepository.findAllByPersonStatus();
    }

    public Optional<PersonsEntity> getPersonById(Long id){
        return Optional.of(personsRepository.findByIdAndByPersonStatus(id, 1));
    }
    public Optional<PersonsEntity> createPerson(PersonsEntity personsEntity){
        return Optional.of(personsRepository.save(personsEntity));
    }
    public Optional<PersonsEntity> updatePerson(Long id, PersonsEntity personsEntity){
          Optional<PersonsEntity> existingPerson = personsRepository.findById(id);
          if(existingPerson.isEmpty()){
              return Optional.empty();
          }
          PersonsEntity person = existingPerson.get();
          person.setPersonName(personsEntity.getPersonName());
          person.setPersonFatherSurname(personsEntity.getPersonFatherSurname());
          person.setPersonMotherSurname(personsEntity.getPersonMotherSurname());
          person.setPersonDni(personsEntity.getPersonDni());
          person.setPersonBirthdate(personsEntity.getPersonBirthdate());
          person.setPersonStatus(personsEntity.getPersonStatus());
          person.setPersonAge(personsEntity.getPersonAge());
          person.setGender(personsEntity.getGender());
          person.setRole(personsEntity.getRole());

          return Optional.of(personsRepository.save(person));
    }
    public Optional<PersonsEntity> deletePerson(Long id){
          Optional<PersonsEntity> existingPerson = personsRepository.findById(id);
          if(existingPerson.isEmpty()){
              return Optional.empty();
          }
          PersonsEntity person = existingPerson.get();
          person.setPersonStatus(0);
          return Optional.of(personsRepository.save(person));
    }


}
