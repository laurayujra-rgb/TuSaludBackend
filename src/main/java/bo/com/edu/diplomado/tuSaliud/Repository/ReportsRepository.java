package bo.com.edu.diplomado.tuSaliud.Repository;

import bo.com.edu.diplomado.tuSaliud.Entity.ReportsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportsRepository extends JpaRepository<ReportsEntity, Long> {


    @Query("select r from ReportsEntity r where r.reportStatus = 1 order by r.reportId asc")
    List<ReportsEntity> findAllByStatus();

    @Query("select r from ReportsEntity r where r.reportId = ?1 and r.reportStatus = ?2")
    ReportsEntity findByIdAndByStatus(Long id, long status);

    @Query("select r from ReportsEntity r where r.kardex.kardexId = ?1 and r.reportStatus = 1 order by r.reportId asc")
    List<ReportsEntity> findAllByKardexId(Long kardexId);
}
