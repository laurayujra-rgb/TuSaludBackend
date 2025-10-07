package bo.com.edu.diplomado.tuSaliud.Service;

import bo.com.edu.diplomado.tuSaliud.Entity.BedsEntity;
import bo.com.edu.diplomado.tuSaliud.Repository.BedsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BedsService {

    @Autowired
    public BedsRepository bedsRepository;

    // 🔹 Todas las camas activas
    public List<BedsEntity> getAllBedsByStatus() {
        return bedsRepository.findAllByStatus();
    }

    // 🔹 Buscar cama activa por ID
    public Optional<BedsEntity> getBedById(Long id) {
        return Optional.ofNullable(bedsRepository.findByIdAndByStatus(id, 1L));
    }

    // 🔹 Crear nueva cama (solo se usa desde BedsController)
    public Optional<BedsEntity> createBed(BedsEntity bedsEntity) {
        bedsEntity.setBedStatus(1);
        bedsEntity.setBedOccupied(false); // una cama nueva está libre
        return Optional.of(bedsRepository.save(bedsEntity));
    }

    // 🔹 Actualizar cama existente (estado o datos)
    public Optional<BedsEntity> updateBed(BedsEntity bed) {
        if (bed.getBedStatus() == null) {
            bed.setBedStatus(1);
        }
        return Optional.of(bedsRepository.save(bed));
    }

    // 🔹 Eliminar cama (soft delete)
    public Optional<BedsEntity> deleteBed(Long id) {
        Optional<BedsEntity> existing = bedsRepository.findById(id);
        if (existing.isEmpty()) return Optional.empty();

        BedsEntity bed = existing.get();
        bed.setBedStatus(0);
        return Optional.of(bedsRepository.save(bed));
    }

    // 🔹 Camas por habitación
    public List<BedsEntity> getBedsByRoomId(Long roomId) {
        return bedsRepository.findByRoomIdAndStatus(roomId);
    }

    // 🔹 Camas disponibles (no ocupadas)
    public List<BedsEntity> getAvailableBeds() {
        return bedsRepository.findAvailableBeds();
    }

    // 🔹 Camas ocupadas
    public List<BedsEntity> getOccupiedBeds() {
        return bedsRepository.findOccupiedBeds();
    }
}
