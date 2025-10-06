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

    // ✅ Listar todas
    public List<BedsEntity> getAllBeds() {
        return bedsRepository.findAll();
    }

    // ✅ Listar solo activas
    public List<BedsEntity> getAllBedsByStatus() {
        return bedsRepository.findAllByStatus();
    }

    // ✅ Buscar cama activa por ID
    public Optional<BedsEntity> getBedById(Long id) {
        return Optional.ofNullable(bedsRepository.findByIdAndByStatus(id, 1L));
    }

    // ✅ Crear cama
    public Optional<BedsEntity> createBed(BedsEntity bedsEntity) {
        bedsEntity.setBedOccupied(false); // por defecto libre
        return Optional.of(bedsRepository.save(bedsEntity));
    }

    // ✅ Actualizar cama
    public Optional<BedsEntity> updateBed(Long id, BedsEntity bedsEntity) {
        BedsEntity bed = bedsRepository.findByIdAndByStatus(id, 1L);
        bed.setBedName(bedsEntity.getBedName());
        bed.setRoom(bedsEntity.getRoom());
        return Optional.of(bedsRepository.save(bed));
    }

    // ✅ Eliminar (soft delete)
    public Optional<BedsEntity> deleteBed(Long id) {
        Optional<BedsEntity> existingBed = bedsRepository.findById(id);
        if (existingBed.isEmpty()) return Optional.empty();
        BedsEntity bed = existingBed.get();
        bed.setBedStatus(0);
        return Optional.of(bedsRepository.save(bed));
    }

    // ✅ Camas por habitación
    public List<BedsEntity> getBedsByRoomId(Long roomId) {
        return bedsRepository.findByRoomIdAndStatus(roomId);
    }

    // ✅ Camas disponibles
    public List<BedsEntity> getAvailableBeds() {
        return bedsRepository.findAvailableBeds();
    }

    // ✅ Camas ocupadas
    public List<BedsEntity> getOccupiedBeds() {
        return bedsRepository.findOccupiedBeds();
    }
}
