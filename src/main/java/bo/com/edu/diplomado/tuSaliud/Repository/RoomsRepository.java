package bo.com.edu.diplomado.tuSaliud.Repository;

import bo.com.edu.diplomado.tuSaliud.Entity.PersonsEntity;
import bo.com.edu.diplomado.tuSaliud.Entity.RoomsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomsRepository extends JpaRepository<RoomsEntity, Long> {

    @Query("select b from RoomsEntity  b where b.roomStatus = 1 order by b.roomId asc")
    List<RoomsEntity> findAllByRoomsStatus();
    @Query("select b from RoomsEntity b where b.roomId=?1 and b.roomStatus=?2")
    RoomsEntity findByIdAndByRoomsStatus(Long id, long status);
}