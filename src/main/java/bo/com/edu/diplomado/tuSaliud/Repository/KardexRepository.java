package bo.com.edu.diplomado.tuSaliud.Repository;

import bo.com.edu.diplomado.tuSaliud.Entity.KardexEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KardexRepository extends JpaRepository<KardexEntity, Long> {
    @Query("select b from KardexEntity b where b.kardexStatus = 1 order by b.kardexId asc")
    List<KardexEntity> findAllByStatus();
    @Query("select b from KardexEntity b where b.kardexId=?1 and b.kardexStatus=?2")
    KardexEntity findByIdAndByStatus(Long id, long status);

}
