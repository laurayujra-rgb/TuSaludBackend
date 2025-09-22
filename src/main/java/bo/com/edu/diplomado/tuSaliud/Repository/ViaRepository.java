package bo.com.edu.diplomado.tuSaliud.Repository;

import bo.com.edu.diplomado.tuSaliud.Entity.KardexEntity;
import bo.com.edu.diplomado.tuSaliud.Entity.ViaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ViaRepository extends JpaRepository<ViaEntity, Long> {
    @Query("select b from ViaEntity  b where b.viaStatus = 1 order by b.viaId asc")
    List<ViaEntity> findAllByStatus();
    @Query("select b from ViaEntity b where b.viaId=?1 and b.viaStatus=?2")
    ViaEntity findByIdAndByStatus(Long id, long status);
}
