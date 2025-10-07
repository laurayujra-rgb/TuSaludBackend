package bo.com.edu.diplomado.tuSaliud.Service;

import bo.com.edu.diplomado.tuSaliud.Entity.BedsEntity;
import bo.com.edu.diplomado.tuSaliud.Entity.PersonsEntity;
import bo.com.edu.diplomado.tuSaliud.Repository.PersonsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonsService {

    @Autowired
    private PersonsRepository personsRepository;

    @Autowired
    private BedsService bedsService;

    @Autowired
    private RoomsService roomsService;

    // 🔹 Obtener todos los registros
    public List<PersonsEntity> gettAllPersons() {
        return personsRepository.findAll();
    }

    // 🔹 Obtener solo personas activas
    public List<PersonsEntity> getAllPersonsByStatus() {
        return personsRepository.findAllByPersonStatus();
    }

    // 🔹 Obtener persona activa por ID
    public Optional<PersonsEntity> getPersonById(Long id) {
        return Optional.ofNullable(personsRepository.findByIdAndByPersonStatus(id, 1));
    }

    // 🔹 Crear persona (paciente o no)
    public Optional<PersonsEntity> createPerson(PersonsEntity personsEntity) {

        if (personsEntity.getRole().getRoleId() == 4) {
            // Paciente → necesita cama
            if (personsEntity.getBed() == null || personsEntity.getBed().getBedId() == 0) {
                throw new RuntimeException("Debe seleccionar una cama para el paciente");
            }

            Optional<BedsEntity> bedOpt = bedsService.getBedById(personsEntity.getBed().getBedId());
            if (bedOpt.isEmpty()) {
                throw new RuntimeException("La cama seleccionada no existe");
            }

            BedsEntity bedFromDb = bedOpt.get();

            if (Boolean.TRUE.equals(bedFromDb.getBedOccupied())) {
                throw new RuntimeException("La cama seleccionada ya está ocupada");
            }

            // ✅ Marcar como ocupada y actualizar
            bedFromDb.setBedOccupied(true);
            bedsService.updateBed(bedFromDb);

            personsEntity.setBed(bedFromDb);
        } else {
            // No es paciente → no tiene cama asignada
            personsEntity.setBed(null);
        }

        PersonsEntity saved = personsRepository.save(personsEntity);
        return Optional.of(saved);
    }

    // 🔹 Actualizar persona (incluye cambio o liberación de cama)
    public Optional<PersonsEntity> updatePerson(Long id, PersonsEntity personsEntity) {
        Optional<PersonsEntity> existingOpt = personsRepository.findById(id);
        if (existingOpt.isEmpty()) return Optional.empty();

        PersonsEntity person = existingOpt.get();

        // Datos generales
        person.setPersonName(personsEntity.getPersonName());
        person.setPersonFatherSurname(personsEntity.getPersonFatherSurname());
        person.setPersonMotherSurname(personsEntity.getPersonMotherSurname());
        person.setPersonDni(personsEntity.getPersonDni());
        person.setPersonBirthdate(personsEntity.getPersonBirthdate());
        person.setPersonStatus(personsEntity.getPersonStatus());
        person.setPersonAge(personsEntity.getPersonAge());
        person.setGender(personsEntity.getGender());
        person.setRole(personsEntity.getRole());

        // 🔹 Si sigue siendo paciente
        if (personsEntity.getRole().getRoleId() == 4) {
            if (personsEntity.getBed() != null && personsEntity.getBed().getBedId() != 0) {
                Optional<BedsEntity> newBedOpt = bedsService.getBedById(personsEntity.getBed().getBedId());
                if (newBedOpt.isEmpty()) {
                    throw new RuntimeException("La cama seleccionada no existe");
                }

                BedsEntity newBed = newBedOpt.get();

                // ⚠️ Si la cama está ocupada por otro paciente
                if (Boolean.TRUE.equals(newBed.getBedOccupied()) &&
                        (person.getBed() == null || newBed.getBedId() != person.getBed().getBedId())) {
                    throw new RuntimeException("La cama seleccionada ya está ocupada");
                }

                // Liberar la cama anterior si cambió
                if (person.getBed() != null && newBed.getBedId() != person.getBed().getBedId()) {
                    BedsEntity oldBed = person.getBed();
                    oldBed.setBedOccupied(false);
                    bedsService.updateBed(oldBed);
                }

                // Ocupamos la nueva
                newBed.setBedOccupied(true);
                bedsService.updateBed(newBed);
                person.setBed(newBed);

            } else if (personsEntity.getBed() == null && person.getBed() != null) {
                // Si quitamos la cama → liberar
                BedsEntity oldBed = person.getBed();
                oldBed.setBedOccupied(false);
                bedsService.updateBed(oldBed);
                person.setBed(null);
            }

        } else {
            // Ya no es paciente → liberar cama si tenía
            if (person.getBed() != null) {
                BedsEntity oldBed = person.getBed();
                oldBed.setBedOccupied(false);
                bedsService.updateBed(oldBed);
                person.setBed(null);
            }
        }

        return Optional.of(personsRepository.save(person));
    }

    // 🔹 Eliminar persona (soft delete y libera cama)
    public Optional<PersonsEntity> deletePerson(Long id) {
        Optional<PersonsEntity> existing = personsRepository.findById(id);
        if (existing.isEmpty()) return Optional.empty();

        PersonsEntity person = existing.get();
        person.setPersonStatus(0);

        if (person.getBed() != null) {
            BedsEntity bed = person.getBed();
            bed.setBedOccupied(false);
            bedsService.updateBed(bed);
        }

        return Optional.of(personsRepository.save(person));
    }

    // 🔹 Obtener personas por rol
    public List<PersonsEntity> getPersonsByRoleId(Long roleId) {
        return personsRepository.findAllByRoleId(roleId);
    }

    public List<PersonsEntity> getPatientsByRoomId(Long roomId) {
        return personsRepository.findActivePatientsByRoomId(roomId);
    }
}
