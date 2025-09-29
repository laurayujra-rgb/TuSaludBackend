package bo.com.edu.diplomado.tuSaliud.Service;

import bo.com.edu.diplomado.tuSaliud.Entity.KardexEntity;
import bo.com.edu.diplomado.tuSaliud.Models.Dto.KardexDto;
import bo.com.edu.diplomado.tuSaliud.Repository.KardexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class KardexService {

    @Autowired
    private KardexRepository kardexRepository;

    // ===== Mapper: Entity → DTO
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
                entity.getDiets() != null ? entity.getDiets().getDietName() : null
        );
    }

    public List<KardexDto> toDtoList(List<KardexEntity> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    // ===== Listar todo
    public List<KardexEntity> getAllKardex() {
        return kardexRepository.findAll();
    }

    // ===== Listar solo activos
    public List<KardexEntity> getAllKardexByStatus() {
        return kardexRepository.findAllByStatus();
    }

    // ===== Buscar por id
    public Optional<KardexEntity> getKardexById(Long id) {
        return Optional.ofNullable(kardexRepository.findByIdAndByStatus(id, 1L));
    }

    // ===== Crear
    public Optional<KardexEntity> createKardex(KardexEntity kardexEntity) {
        return Optional.of(kardexRepository.save(kardexEntity));
    }

    // ===== Update
    public Optional<KardexEntity> updateKardex(Long id, KardexEntity kardexEntity) {
        Optional<KardexEntity> existingKardex = kardexRepository.findById(id);
        if (existingKardex.isEmpty()) {
            return Optional.empty();
        }
        KardexEntity kardex = kardexRepository.findByIdAndByStatus(id, 1L);
        kardex.setKardexNumber(kardexEntity.getKardexNumber());
        kardex.setKardexDiagnosis(kardexEntity.getKardexDiagnosis());
        kardex.setKardexDate(kardexEntity.getKardexDate());
        kardex.setKardexHour(kardexEntity.getKardexHour());
        kardex.setNursingActions(kardexEntity.getNursingActions());
        kardex.setDiets(kardexEntity.getDiets());
        return Optional.of(kardexRepository.save(kardex));
    }

    // ===== Soft delete
    public Optional<KardexEntity> deleteKardex(Long id) {
        Optional<KardexEntity> kardexEntity = kardexRepository.findById(id);
        if (kardexEntity.isEmpty()) {
            return Optional.empty();
        }
        KardexEntity kardex = kardexEntity.get();
        kardex.setKardexStatus(0);
        return Optional.of(kardexRepository.save(kardex));
    }
}
