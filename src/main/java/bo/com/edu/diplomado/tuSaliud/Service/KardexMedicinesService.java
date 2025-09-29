package bo.com.edu.diplomado.tuSaliud.Service;

import bo.com.edu.diplomado.tuSaliud.Entity.*;
import bo.com.edu.diplomado.tuSaliud.Models.Dto.KardexMedicineDto;
import bo.com.edu.diplomado.tuSaliud.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class KardexMedicinesService {

    @Autowired
    private KardexMedicinesRepository kardexMedicinesRepository;

    @Autowired
    private KardexRepository kardexRepository;

    @Autowired
    private MedicinesRepository medicinesRepository;

    // ===== Mapper: Entity → DTO
    public KardexMedicineDto toDto(KardexMedicinesEntity entity) {
        if (entity == null) return null;

        return new KardexMedicineDto(
                entity.getId(),
                entity.getKardex() != null ? entity.getKardex().getKardexId() : null,
                entity.getMedicine() != null ? entity.getMedicine().getMedicineId() : null,
                entity.getMedicine() != null ? entity.getMedicine().getMedicineName() : null,
                entity.getDose(),
                entity.getFrequency(),
                entity.getRouteNote(),
                entity.getNotes(),
                entity.getStatus()
        );
    }

    public List<KardexMedicineDto> toDtoList(List<KardexMedicinesEntity> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    // ===== Listar todo
    public List<KardexMedicinesEntity> getAll() {
        return kardexMedicinesRepository.findAll();
    }

    // ===== Buscar por id
    public Optional<KardexMedicinesEntity> getById(Long id) {
        return kardexMedicinesRepository.findById(id);
    }

    // ===== Listar por kardexId
    public List<KardexMedicinesEntity> getByKardexId(Long kardexId) {
        return kardexMedicinesRepository.findByKardex_KardexId(kardexId);
    }

    // ===== Listar por medicineId
    public List<KardexMedicinesEntity> getByMedicineId(Long medicineId) {
        return kardexMedicinesRepository.findByMedicine_MedicineId(medicineId);
    }

    // ===== Crear relación
    public Optional<KardexMedicinesEntity> create(KardexMedicinesEntity km) {
        KardexEntity kardexActivo = kardexRepository.findByIdAndByStatus(
                km.getKardex().getKardexId(), 1L
        );
        if (kardexActivo == null) throw new IllegalArgumentException("Kardex no encontrado");

        MedicinesEntity med = medicinesRepository.findById(km.getMedicine().getMedicineId())
                .orElse(null);
        if (med == null) throw new IllegalArgumentException("Medicamento no encontrado");

        boolean exists = kardexMedicinesRepository
                .existsByKardex_KardexIdAndMedicine_MedicineId(kardexActivo.getKardexId(), med.getMedicineId());
        if (exists) throw new IllegalStateException("El medicamento ya está asignado a este kardex");

        km.setKardex(kardexActivo);
        km.setMedicine(med);
        if (km.getStatus() == null) km.setStatus(1);

        return Optional.of(kardexMedicinesRepository.save(km));
    }

    // ===== Update
    public Optional<KardexMedicinesEntity> update(Long id, KardexMedicinesEntity data) {
        Optional<KardexMedicinesEntity> opt = kardexMedicinesRepository.findById(id);
        if (opt.isEmpty()) return Optional.empty();

        KardexMedicinesEntity current = opt.get();

        if (data.getDose() != null) current.setDose(data.getDose());
        if (data.getFrequency() != null) current.setFrequency(data.getFrequency());
        if (data.getRouteNote() != null) current.setRouteNote(data.getRouteNote());
        if (data.getNotes() != null) current.setNotes(data.getNotes());
        if (data.getStatus() != null) current.setStatus(data.getStatus());

        return Optional.of(kardexMedicinesRepository.save(current));
    }

    // ===== Soft delete
    public Optional<KardexMedicinesEntity> delete(Long id) {
        Optional<KardexMedicinesEntity> opt = kardexMedicinesRepository.findById(id);
        if (opt.isEmpty()) return Optional.empty();
        KardexMedicinesEntity km = opt.get();
        km.setStatus(0);
        return Optional.of(kardexMedicinesRepository.save(km));
    }

    public void deleteByPair(Long kardexId, Long medicineId) {
        kardexMedicinesRepository.deleteByKardex_KardexIdAndMedicine_MedicineId(kardexId, medicineId);
    }
}
