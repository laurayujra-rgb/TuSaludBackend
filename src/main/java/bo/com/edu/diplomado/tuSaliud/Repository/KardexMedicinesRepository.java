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

    // Si luego quieres filtrar por status=1, puedes crear:
    // List<KardexMedicinesEntity> findByKardex_KardexIdAndStatus(Long kardexId, Integer status);
    // List<KardexMedicinesEntity> findByMedicine_MedicineIdAndStatus(Long medicineId, Integer status);
    // Optional<KardexMedicinesEntity> findByIdAndStatus(Long id, Integer status);
}
