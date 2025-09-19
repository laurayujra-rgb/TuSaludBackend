package bo.com.edu.diplomado.tuSaliud.Repository;

import bo.com.edu.diplomado.tuSaliud.Entity.GendersEntity;
import bo.com.edu.diplomado.tuSaliud.Entity.HospitalsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HospitalsRepository extends JpaRepository<HospitalsEntity, Long> {
    // Listar activos
    @Query("select h from HospitalsEntity h where h.hospitalStatus = 1 order by h.hospitalId asc")
    List<HospitalsEntity> findAllByStatus();

    // Buscar por id + status (usa Integer, no long)
    @Query("select h from HospitalsEntity h where h.hospitalId = ?1 and h.hospitalStatus = ?2")
    Optional<HospitalsEntity> findByIdAndByStatus(Long id, Integer status);

}
