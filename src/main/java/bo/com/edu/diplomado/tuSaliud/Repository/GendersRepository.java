package bo.com.edu.diplomado.tuSaliud.Repository;

import bo.com.edu.diplomado.tuSaliud.Entity.GendersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GendersRepository extends JpaRepository<GendersEntity, Long> {
    @Query("select b from GendersEntity b where b.genderStatus = 1 order by b.genderId asc")
    List<GendersEntity> findAllByStatus();
    @Query("select b from GendersEntity b where b.genderId=?1 and b.genderStatus=?2")
    GendersEntity findByIdAndByStatus(Long id, long status);
}
