package bo.com.edu.diplomado.tuSaliud.Repository;

import bo.com.edu.diplomado.tuSaliud.Entity.PersonsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonsRepository extends JpaRepository<PersonsEntity, Long> {
    @Query("select b from PersonsEntity  b where b.personStatus = 1 order by b.personId asc")
    List<PersonsEntity> findAllByPersonStatus();
    @Query("select b from PersonsEntity b where b.personId=?1 and b.personStatus=?2")
    PersonsEntity findByIdAndByPersonStatus(Long id, long status);
    @Query("select p from PersonsEntity p where p.role.roleId = ?1 and p.personStatus = 1 order by p.personId asc")
    List<PersonsEntity> findAllByRoleId(Long roleId);

    @Query("""
           select p
           from PersonsEntity p
           join p.bed b
           join b.room r
           where p.personStatus = 1
             and p.role.roleId = 4
             and r.roomId = :roomId
           order by p.personId asc
           """)
    List<PersonsEntity> findActivePatientsByRoomId(@Param("roomId") Long roomId);
}
