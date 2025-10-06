package bo.com.edu.diplomado.tuSaliud.Repository;

import bo.com.edu.diplomado.tuSaliud.Entity.RoomsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface RoomsRepository extends JpaRepository<RoomsEntity, Long> {

    @Query("SELECT r FROM RoomsEntity r WHERE r.roomStatus = 1 ORDER BY r.roomId ASC")
    List<RoomsEntity> findAllByRoomsStatus();

    @Query("SELECT r FROM RoomsEntity r WHERE r.roomId = ?1 AND r.roomStatus = ?2")
    RoomsEntity findByIdAndByRoomsStatus(Long id, long status);

    // 🔹 Salas disponibles
    @Query("SELECT r FROM RoomsEntity r WHERE r.roomStatus = 1 AND r.roomOccupied = false ORDER BY r.roomId ASC")
    List<RoomsEntity> findAvailableRooms();

    // 🔹 Salas ocupadas
    @Query("SELECT r FROM RoomsEntity r WHERE r.roomStatus = 1 AND r.roomOccupied = true ORDER BY r.roomId ASC")
    List<RoomsEntity> findOccupiedRooms();
}
