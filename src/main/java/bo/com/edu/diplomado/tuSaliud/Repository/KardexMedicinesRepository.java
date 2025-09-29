package bo.com.edu.diplomado.tuSaliud.Repository;

import bo.com.edu.diplomado.tuSaliud.Entity.KardexMedicinesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KardexMedicinesRepository extends JpaRepository<KardexMedicinesEntity, Long> {

    List<KardexMedicinesEntity> findByKardex_KardexId(Long kardexId);

    List<KardexMedicinesEntity> findByMedicine_MedicineId(Long medicineId);

    boolean existsByKardex_KardexIdAndMedicine_MedicineId(Long kardexId, Long medicineId);

    void deleteByKardex_KardexIdAndMedicine_MedicineId(Long kardexId, Long medicineId);


}
