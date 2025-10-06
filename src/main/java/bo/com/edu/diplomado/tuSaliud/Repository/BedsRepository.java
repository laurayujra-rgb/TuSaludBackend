package bo.com.edu.diplomado.tuSaliud.Repository;

import bo.com.edu.diplomado.tuSaliud.Entity.BedsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BedsRepository extends JpaRepository<BedsEntity, Long> {

    // 🔹 Todas las camas activas
    @Query("SELECT b FROM BedsEntity b WHERE b.bedStatus = 1 ORDER BY b.bedId ASC")
    List<BedsEntity> findAllByStatus();

    // 🔹 Cama específica activa
    @Query("SELECT b FROM BedsEntity b WHERE b.bedId = ?1 AND b.bedStatus = ?2")
    BedsEntity findByIdAndByStatus(Long id, long status);

    // 🔹 Camas activas por sala
    @Query("SELECT b FROM BedsEntity b WHERE b.room.roomId = :roomId AND b.bedStatus = 1")
    List<BedsEntity> findByRoomIdAndStatus(Long roomId);

    // 🔹 Camas disponibles (no ocupadas)
    @Query("SELECT b FROM BedsEntity b WHERE b.bedStatus = 1 AND b.bedOccupied = false ORDER BY b.bedId ASC")
    List<BedsEntity> findAvailableBeds();

    // 🔹 Camas ocupadas
    @Query("SELECT b FROM BedsEntity b WHERE b.bedStatus = 1 AND b.bedOccupied = true ORDER BY b.bedId ASC")
    List<BedsEntity> findOccupiedBeds();
}
