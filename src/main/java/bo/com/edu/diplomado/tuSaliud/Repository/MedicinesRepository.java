package bo.com.edu.diplomado.tuSaliud.Repository;

import bo.com.edu.diplomado.tuSaliud.Entity.MedicinesEntity;
import bo.com.edu.diplomado.tuSaliud.Entity.ViaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MedicinesRepository extends JpaRepository<MedicinesEntity, Long> {

    @Query("select b from MedicinesEntity  b where b.medicineStatus = 1 order by b.medicineId asc")
    List<MedicinesEntity> findAllByStatus();
    @Query("select b from MedicinesEntity b where b.medicineId=?1 and b.medicineStatus=?2")
    MedicinesEntity findByIdAndByStatus(Long id, long status);
}
