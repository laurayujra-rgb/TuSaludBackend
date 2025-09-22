package bo.com.edu.diplomado.tuSaliud.Repository;

import bo.com.edu.diplomado.tuSaliud.Entity.DietsEntity;
import bo.com.edu.diplomado.tuSaliud.Entity.GendersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DietsRepository extends JpaRepository<DietsEntity, Long> {
    @Query("select b from DietsEntity b where b.dietStatus = 1 order by b.dietId asc")
    List<DietsEntity> findAllByStatus();
    @Query("select b from DietsEntity b where b.dietId=?1 and b.dietStatus=?2")
    DietsEntity findByIdAndByStatus(Long id, long status);
}
