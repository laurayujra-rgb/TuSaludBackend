package bo.com.edu.diplomado.tuSaliud.Repository;

import bo.com.edu.diplomado.tuSaliud.Entity.BedsEntity;
import bo.com.edu.diplomado.tuSaliud.Entity.GendersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BedsRepository extends JpaRepository<BedsEntity, Long> {

    @Query("select b from BedsEntity b where b.bedStatus = 1 order by b.bedId asc")
    List<BedsEntity> findAllByStatus();
    @Query("select b from BedsEntity b where b.bedId=?1 and b.bedStatus=?2")
    BedsEntity findByIdAndByStatus(Long id, long status);

    // 🔹 Nuevo método: buscar todas las camas por roomId y status activo
    @Query("SELECT b FROM BedsEntity b WHERE b.room.roomId = :roomId AND b.bedStatus = 1")
    List<BedsEntity> findByRoomIdAndStatus(Long roomId);
}
