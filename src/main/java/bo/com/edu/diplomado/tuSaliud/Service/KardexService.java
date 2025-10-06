package bo.com.edu.diplomado.tuSaliud.Service;

import bo.com.edu.diplomado.tuSaliud.Entity.*;
import bo.com.edu.diplomado.tuSaliud.Models.Dto.KardexDto;
import bo.com.edu.diplomado.tuSaliud.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KardexService {

    @Autowired
    private KardexRepository kardexRepository;

    @Autowired
    private BedsRepository bedsRepository;

    @Autowired
    private RoomsRepository roomsRepository;

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
                entity.getPatient() != null ? fullName(entity.getPatient()) : null,
                entity.getNurse() != null ? entity.getNurse().getPersonId() : null,
                entity.getNurse() != null ? fullName(entity.getNurse()) : null,

                entity.getBed() != null ? entity.getBed().getBedId() : null,
                entity.getBed() != null ? entity.getBed().getBedName() : null,
                entity.getBed() != null ? entity.getBed().getRoom().getRoomId() : null,
                entity.getBed() != null ? entity.getBed().getRoom().getRoomName() : null
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

    // ====== Crear Kardex (con control de cama/sala) ======
    public Optional<KardexEntity> createKardex(KardexEntity kardexEntity) {
        BedsEntity bed = kardexEntity.getBed();

        if (bed == null) throw new RuntimeException("Debe asignar una cama al Kardex");

        // Buscar cama real en BD
        BedsEntity bedDB = bedsRepository.findById(bed.getBedId())
                .orElseThrow(() -> new RuntimeException("Cama no encontrada"));

        if (bedDB.getBedStatus() != 1)
            throw new RuntimeException("La cama no está activa");
        if (Boolean.TRUE.equals(bedDB.getBedOccupied()))
            throw new RuntimeException("La cama ya está ocupada");

        // Marcar cama y sala como ocupadas
        bedDB.setBedOccupied(true);
        RoomsEntity room = bedDB.getRoom();
        if (room != null) room.setRoomOccupied(true);

        bedsRepository.save(bedDB);
        if (room != null) roomsRepository.save(room);

        kardexEntity.setBed(bedDB);
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
        kardex.setNurse(kardexEntity.getNurse());

        // Si se cambia de cama
        if (kardexEntity.getBed() != null && kardexEntity.getBed().getBedId() != kardex.getBed().getBedId()) {
            BedsEntity oldBed = kardex.getBed();
            oldBed.setBedOccupied(false);
            bedsRepository.save(oldBed);

            BedsEntity newBed = bedsRepository.findById(kardexEntity.getBed().getBedId())
                    .orElseThrow(() -> new RuntimeException("Nueva cama no encontrada"));
            if (Boolean.TRUE.equals(newBed.getBedOccupied()))
                throw new RuntimeException("La nueva cama ya está ocupada");
            newBed.setBedOccupied(true);
            bedsRepository.save(newBed);

            kardex.setBed(newBed);
        }

        return Optional.of(kardexRepository.save(kardex));
    }

    // ====== Eliminar Kardex (liberar cama/sala) ======
    public Optional<KardexEntity> deleteKardex(Long id) {
        Optional<KardexEntity> opt = kardexRepository.findById(id);
        if (opt.isEmpty()) return Optional.empty();

        KardexEntity kardex = opt.get();
        kardex.setKardexStatus(0);

        BedsEntity bed = kardex.getBed();
        if (bed != null) {
            bed.setBedOccupied(false);
            bedsRepository.save(bed);

            // Si todas las camas están libres, liberar la sala
            RoomsEntity room = bed.getRoom();
            if (room != null) {
                boolean allFree = room.getBeds().stream().noneMatch(BedsEntity::getBedOccupied);
                if (allFree) {
                    room.setRoomOccupied(false);
                    roomsRepository.save(room);
                }
            }
        }

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

    public List<KardexDto> getKardexDtoByRoomId(Long roomId) {
        return kardexRepository.findAllByRoomId(roomId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
