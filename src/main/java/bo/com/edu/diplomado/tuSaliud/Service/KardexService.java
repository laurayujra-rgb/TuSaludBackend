package bo.com.edu.diplomado.tuSaliud.Service;

import bo.com.edu.diplomado.tuSaliud.Entity.KardexEntity;
import bo.com.edu.diplomado.tuSaliud.Entity.PersonsEntity;
import bo.com.edu.diplomado.tuSaliud.Models.Dto.KardexDto;
import bo.com.edu.diplomado.tuSaliud.Repository.KardexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KardexService {

    @Autowired
    private KardexRepository kardexRepository;

    private String fullName(PersonsEntity p) {
        if (p == null) return null;
        String n = (p.getPersonName() == null ? "" : p.getPersonName());
        String f = (p.getPersonFatherSurname() == null ? "" : " " + p.getPersonFatherSurname());
        String m = (p.getPersonMotherSurname() == null ? "" : " " + p.getPersonMotherSurname());
        return (n + f + m).trim().replaceAll("\\s+", " ");
    }

    // ====== Mapper Entity → DTO ======
    public KardexDto toDto(KardexEntity entity) {
        if (entity == null) return null;

        return new KardexDto(
                entity.getKardexId(),
                entity.getKardexNumber(),
                entity.getKardexDiagnosis(),
                entity.getKardexDate(),
                entity.getKardexHour(),
                entity.getKardexStatus(),
                entity.getNursingActions(),

                entity.getDiets() != null ? entity.getDiets().getDietId() : null,
                entity.getDiets() != null ? entity.getDiets().getDietName() : null,

                entity.getPatient() != null ? entity.getPatient().getPersonId() : null,
                entity.getPatient() != null ? fullName(entity.getPatient()) : null
        );
    }

    public List<KardexDto> toDtoList(List<KardexEntity> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    // ====== Métodos base ======
    public List<KardexEntity> getAllKardex() {
        return kardexRepository.findAll();
    }

    public Optional<KardexEntity> getKardexById(Long id) {
        return Optional.ofNullable(kardexRepository.findByIdAndByStatus(id, 1L));
    }

    // ====== Crear Kardex ======
    public Optional<KardexEntity> createKardex(KardexEntity kardexEntity) {
        return Optional.of(kardexRepository.save(kardexEntity));
    }

    // ====== Actualizar Kardex ======
    public Optional<KardexEntity> updateKardex(Long id, KardexEntity kardexEntity) {
        Optional<KardexEntity> existingKardex = kardexRepository.findById(id);
        if (existingKardex.isEmpty()) {
            return Optional.empty();
        }

        KardexEntity kardex = existingKardex.get();
        kardex.setKardexNumber(kardexEntity.getKardexNumber());
        kardex.setKardexDiagnosis(kardexEntity.getKardexDiagnosis());
        kardex.setKardexDate(kardexEntity.getKardexDate());
        kardex.setKardexHour(kardexEntity.getKardexHour());
        kardex.setNursingActions(kardexEntity.getNursingActions());
        kardex.setDiets(kardexEntity.getDiets());
        kardex.setPatient(kardexEntity.getPatient());

        // ❌ Eliminado kardex.setNurse()

        return Optional.of(kardexRepository.save(kardex));
    }

    // ====== Eliminar Kardex ======
    public Optional<KardexEntity> deleteKardex(Long id) {
        Optional<KardexEntity> opt = kardexRepository.findById(id);
        if (opt.isEmpty()) return Optional.empty();

        KardexEntity kardex = opt.get();
        kardex.setKardexStatus(0);
        return Optional.of(kardexRepository.save(kardex));
    }

    // ====== Kardex por paciente ======
    public List<KardexDto> getKardexDtoByPatientId(Long patientId) {
        return kardexRepository.findAllByPatientId(patientId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ====== Kardex por paciente y rol ======
    public List<KardexDto> getKardexDtoByPatientAndRole(Long patientId, Long roleId) {
        return kardexRepository.findAllByPatientIdAndRoleId(patientId, roleId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
