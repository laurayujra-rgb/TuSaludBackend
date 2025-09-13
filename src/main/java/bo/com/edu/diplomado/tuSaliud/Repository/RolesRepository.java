package bo.com.edu.diplomado.tuSaliud.Repository;

import bo.com.edu.diplomado.tuSaliud.Entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RolesRepository extends JpaRepository<RolesEntity, Long> {
    @Query("select b from RolesEntity b where b.roleStatus = 1 order by b.roleId asc")
    List<RolesEntity> findAllByStatus();
    @Query("select b from RolesEntity b where b.roleId=?1 and b.roleStatus=?2")
    RolesEntity findByIdAndByStatus(Long id, long status);
}
