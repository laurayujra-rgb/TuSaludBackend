package bo.com.edu.diplomado.tuSaliud.Service;

import bo.com.edu.diplomado.tuSaliud.Entity.*;
import bo.com.edu.diplomado.tuSaliud.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KardexMedicinesService {

    @Autowired
    private KardexMedicinesRepository kardexMedicinesRepository;

    @Autowired
    private KardexRepository kardexRepository;

    @Autowired
    private MedicinesRepository medicinesRepository;

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

    // ===== Crear relación (valida FKs y duplicado)
    public Optional<KardexMedicinesEntity> create(KardexMedicinesEntity km) {
        // Validar Kardex activo (siguiendo tu patrón de status=1)
        KardexEntity kardexActivo = kardexRepository.findByIdAndByStatus(
                km.getKardex().getKardexId(), 1L
        );
        if (kardexActivo == null) {
            throw new IllegalArgumentException("Kardex no encontrado");
        }

        // Validar Medicine
        MedicinesEntity med = medicinesRepository.findById(km.getMedicine().getMedicineId())
                .orElse(null);
        if (med == null) {
            throw new IllegalArgumentException("Medicamento no encontrado");
        }

        // Duplicado (único kardex_id + medicine_id)
        boolean exists = kardexMedicinesRepository
                .existsByKardex_KardexIdAndMedicine_MedicineId(
                        kardexActivo.getKardexId(), med.getMedicineId()
                );
        if (exists) {
            throw new IllegalStateException("El medicamento ya está asignado a este kardex");
        }

        // Normalizar referencias y status
        km.setKardex(kardexActivo);
        km.setMedicine(med);
        if (km.getStatus() == null) km.setStatus(1);

        return Optional.of(kardexMedicinesRepository.save(km));
    }

    // ===== Update (permite cambiar FKs y datos; valida y evita duplicado)
    public Optional<KardexMedicinesEntity> update(Long id, KardexMedicinesEntity data) {
        Optional<KardexMedicinesEntity> opt = kardexMedicinesRepository.findById(id);
        if (opt.isEmpty()) return Optional.empty();

        KardexMedicinesEntity current = opt.get();

        // Cambiar Kardex si llega y es distinto
        // Cambiar Kardex si llega y es distinto
        if (data.getKardex() != null
                && data.getKardex().getKardexId() != 0
                && (current.getKardex() == null
                || data.getKardex().getKardexId() != current.getKardex().getKardexId())) {

            KardexEntity kardexActivo = kardexRepository.findByIdAndByStatus(
                    data.getKardex().getKardexId(), 1L
            );
            if (kardexActivo == null) {
                throw new IllegalArgumentException("Kardex no encontrado");
            }
            current.setKardex(kardexActivo);
        }

// Cambiar Medicine si llega y es distinto
        if (data.getMedicine() != null
                && data.getMedicine().getMedicineId() != 0
                && (current.getMedicine() == null
                || data.getMedicine().getMedicineId() != current.getMedicine().getMedicineId())) {

            MedicinesEntity med = medicinesRepository.findById(
                    data.getMedicine().getMedicineId()
            ).orElse(null);

            if (med == null) {
                throw new IllegalArgumentException("Medicamento no encontrado");
            }
            current.setMedicine(med);
        }
        // Si cambió combinación FK, validar duplicado
        boolean dup = kardexMedicinesRepository
                .existsByKardex_KardexIdAndMedicine_MedicineId(
                        current.getKardex().getKardexId(),
                        current.getMedicine().getMedicineId()
                );
        if (dup && !current.getId().equals(id)) {
            throw new IllegalStateException("La combinación Kardex–Medicamento ya existe");
        }

        // Campos simples
        if (data.getDose() != null) current.setDose(data.getDose());
        if (data.getFrequency() != null) current.setFrequency(data.getFrequency());
        if (data.getRouteNote() != null) current.setRouteNote(data.getRouteNote());
        if (data.getNotes() != null) current.setNotes(data.getNotes());
        if (data.getStatus() != null) current.setStatus(data.getStatus());

        return Optional.of(kardexMedicinesRepository.save(current));
    }

    // ===== Soft delete por id (status = 0)
    public Optional<KardexMedicinesEntity> delete(Long id) {
        Optional<KardexMedicinesEntity> opt = kardexMedicinesRepository.findById(id);
        if (opt.isEmpty()) return Optional.empty();
        KardexMedicinesEntity km = opt.get();
        km.setStatus(0);
        return Optional.of(kardexMedicinesRepository.save(km));
    }

    // ===== Borrado directo por par FK (si lo necesitas "duro" o "físico")
    public void deleteByPair(Long kardexId, Long medicineId) {
        kardexMedicinesRepository.deleteByKardex_KardexIdAndMedicine_MedicineId(kardexId, medicineId);
    }
}
