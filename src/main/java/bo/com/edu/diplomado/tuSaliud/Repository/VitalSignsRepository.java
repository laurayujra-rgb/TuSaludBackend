package bo.com.edu.diplomado.tuSaliud.Repository;


import bo.com.edu.diplomado.tuSaliud.Entity.GendersEntity;
import bo.com.edu.diplomado.tuSaliud.Entity.VitalSignsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VitalSignsRepository extends JpaRepository<VitalSignsEntity, Long> {
    @Query("select b from VitalSignsEntity b where b.vitalSignsStatus = 1 order by b.vitalSignsId asc")
    List<VitalSignsEntity> findAllByStatus();
    @Query("select b from VitalSignsEntity b where b.vitalSignsId=?1 and b.vitalSignsStatus=?2")
    VitalSignsEntity findByIdAndByStatus(Long id, long status);

    List<VitalSignsEntity> findByKardex_KardexIdAndVitalSignsStatus(Long kardexId, Integer status);

}
