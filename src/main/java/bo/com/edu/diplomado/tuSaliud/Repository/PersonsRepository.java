package bo.com.edu.diplomado.tuSaliud.Repository;

import bo.com.edu.diplomado.tuSaliud.Entity.PersonsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonsRepository extends JpaRepository<PersonsEntity, Long> {
    @Query("select b from PersonsEntity  b where b.personId = 1 order by b.personId asc")
    List<PersonsEntity> findAllByPersonStatus();
    @Query("select b from PersonsEntity b where b.personId=?1 and b.personStatus=?2")
    PersonsEntity findByIdAndByPersonStatus(Long id, long status);
}
